package org.example.servlets;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dto.PlayerDTO;
import org.example.model.Player;
import org.example.security.JWTUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Сервлет для авторизации игроков в системе.
 * <p>
 * Обрабатывает POST-запросы на авторизацию, проверяет введенные пользователем учетные данные,
 * генерирует JWT токен и устанавливает информацию об авторизованном пользователе в сессии.
 * </p>
 */
@WebServlet("/login")
public class LoginServlet extends BaseServlet {
    private static final Logger LOGGER = LogManager.getLogger(LoginServlet.class);
    private final ObjectMapper objectMapper;
    private final JWTUtil jwtUtil;

    /**
     * Конструктор по умолчанию, инициализирующий ObjectMapper и JWTUtil.
     */
    public LoginServlet() {
        this.objectMapper = new ObjectMapper();
        this.jwtUtil = new JWTUtil();
    }

    /**
     * Обрабатывает POST-запросы на авторизацию.
     * <p>
     * Читает учетные данные из запроса, пытается авторизовать пользователя в системе,
     * генерирует JWT токен и отправляет его в ответе в случае успешной авторизации.
     * </p>
     *
     * @param req  HttpServletRequest
     * @param resp HttpServletResponse
     * @throws IOException в случае ошибок ввода-вывода
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            PlayerDTO loginDTO = readLoginDTO(req);


            Player loggedInPlayer = walletServiceFacade.authorizePlayer(loginDTO.getUsername(), loginDTO.getPassword());
            if (loggedInPlayer != null) {
                HttpSession session = req.getSession();
                session.setAttribute("loggedInPlayer", loggedInPlayer);
                String role;
                if("admin".equals(loggedInPlayer.getUsername())) {
                    role = "ADMIN";
                } else {
                    role = "USER";
                }
                String token = jwtUtil.generateToken(loggedInPlayer.getUsername(), role);
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write("{\"status\":\"success\", \"message\":\"Успешная авторизация\", " +
                        "\"token\":\"" + token + "\"}");
            } else {
                sendErrorResponse(resp, HttpServletResponse.SC_UNAUTHORIZED,
                        "Ошибка авторизации. Неправильное имя пользователя или пароль.");
            }
        } catch (JsonParseException | JsonMappingException e) {
            sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Неверный JSON формат");
            LOGGER.error("Неверный JSON формат ", e);
        } catch (Exception e) {
            sendErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка на сервере");
        }
    }

    /**
     * Преобразует входящий поток запроса в объект {@link PlayerDTO}.
     *
     * @param req HttpServletRequest, содержащий входящий поток с данными для авторизации.
     * @return Объект {@link PlayerDTO}, содержащий данные для авторизации.
     * @throws IOException В случае ошибок ввода-вывода при чтении из входящего потока.
     */
    private PlayerDTO readLoginDTO(HttpServletRequest req) throws IOException {
        return objectMapper.readValue(req.getInputStream(), PlayerDTO.class);
    }

    /**
     * Отправляет ответ с кодом ошибки и сообщением об ошибке в формате JSON.
     *
     * @param resp HttpServletResponse, через который будет отправлен ответ.
     * @param status Код статуса HTTP ответа.
     * @param errorMessage Сообщение об ошибке для отправки клиенту.
     * @throws IOException В случае ошибок ввода-вывода при записи в поток ответа.
     */
    private void sendErrorResponse(HttpServletResponse resp, int status, String errorMessage) throws IOException {
        resp.setStatus(status);
        resp.getWriter().write(String.format("{\"error\":\"%s\"}", errorMessage));
    }
}

