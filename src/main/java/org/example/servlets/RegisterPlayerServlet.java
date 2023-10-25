package org.example.servlets;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dto.PlayerDTO;

import org.example.model.Player;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * Сервлет, обрабатывающий регистрацию новых игроков.
 */
@WebServlet("/players")
public class RegisterPlayerServlet extends BaseServlet {
    private static final Logger LOGGER = LogManager.getLogger(RegisterPlayerServlet.class);
    private final ObjectMapper objectMapper;

    /**
     * Конструктор для инициализации ObjectMapper.
     */
    public RegisterPlayerServlet() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Обрабатывает POST-запрос на регистрацию нового игрока.
     *
     * @param req  HttpServletRequest, представляющий запрос клиента к сервлету.
     * @param resp HttpServletResponse, представляющий ответ, который сервлет отправляет клиенту.
     * @throws IOException В случае ошибок ввода-вывода при записи в поток ответа.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            PlayerDTO registrationDTO = objectMapper.readValue(req.getInputStream(), PlayerDTO.class);

            Optional<Player> registerPlayer = walletServiceFacade
                    .registerPlayer(registrationDTO.getUsername(), registrationDTO.getPassword());
            if (registerPlayer.isPresent()) {
                resp.setStatus(HttpServletResponse.SC_CREATED);
                resp.getWriter().write("{\"status\":\"success\", \"message\":\"Регистрация прошла успешно\"}");
            } else {
                sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Ошибка регистрации");
            }
        } catch (JsonParseException | JsonMappingException e) {
            sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Неверный JSON формат");
            LOGGER.error("Неверный JSON формат ", e);
        } catch (Exception e) {
            sendErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка на сервере");
        }
    }

    /**
     * Отправляет ответ с ошибкой клиенту.
     *
     * @param resp        HttpServletResponse, представляющий ответ, который сервлет отправляет клиенту.
     * @param status      HTTP статус-код ответа.
     * @param errorMessage Сообщение об ошибке.
     * @throws IOException В случае ошибок ввода-вывода при записи в поток ответа.
     */
    private void sendErrorResponse(HttpServletResponse resp, int status, String errorMessage) throws IOException {
        resp.setStatus(status);
        resp.getWriter().write(String.format("{\"error\":\"%s\"}", errorMessage));
    }

}
