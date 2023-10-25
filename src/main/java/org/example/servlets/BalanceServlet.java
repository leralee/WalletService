package org.example.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.BalanceDTO;
import org.example.exception.InvalidAmountException;
import org.example.exception.TransactionExistsException;
import org.example.model.Player;
import org.example.security.AdminAuthFilter;
import org.example.security.JWTUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.UUID;


/**
 * Сервлет для обработки операций с балансом игрока.
 * <p>
 * Данный сервлет предоставляет точки доступа для пополнения и снятия средств с баланса игрока.
 * </p>
 */
@WebServlet("/balances")
public class BalanceServlet extends BaseServlet {


    /**
     * Обрабатывает POST-запросы для пополнения или снятия средств.
     * <p>
     * Тело запроса должно содержать JSON-представление {@link BalanceDTO}.
     * </p>
     *
     * @param req  Объект HttpServletRequest, содержащий запрос, сделанный клиентом.
     * @param resp Объект HttpServletResponse, содержащий ответ, отправляемый клиенту.
     * @throws ServletException если запрос не может быть обработан.
     * @throws IOException      при обнаружении ошибки ввода или вывода.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");


        Player player = (Player) req.getSession().getAttribute("loggedInPlayer");

        if (player == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().println("Пользователь не авторизован");
            return;
        }

        String token = AdminAuthFilter.extractTokenFromHeader(req);

        if (token == null || !JWTUtil.validateToken(token)) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().println("Невалидный токен");
            return;
        }

        BalanceDTO balanceDTO;

        try {
            balanceDTO = new ObjectMapper().readValue(req.getInputStream(), BalanceDTO.class);
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("Неверный формат запроса");
            return;
        }

        try {
            switch (balanceDTO.getAction()) {
                case "deposit":
                    walletServiceFacade.credit(player, balanceDTO.getAmount(),
                            UUID.fromString(balanceDTO.getTransaction_id()));
                    break;
                case "withdraw":
                    BigDecimal currentBalance = walletServiceFacade.getPlayerBalance(player.getId());
                    if (currentBalance.compareTo(balanceDTO.getAmount()) >= 0) {
                        walletServiceFacade.withdraw(player, balanceDTO.getAmount(), UUID.fromString(balanceDTO.getTransaction_id()));
                    } else {
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        resp.getWriter().println("Недостаточно средств");
                        return;
                    }
                    break;
                default:
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().println("Неверное действие");
                    return;
            }
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println("Текущий баланс: " + walletServiceFacade.getPlayerBalance(player.getId()));
        } catch (InvalidAmountException | TransactionExistsException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println(e.getMessage());
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println("Ошибка на сервере");
        }
    }

    /**
     * Обрабатывает GET-запросы для получения текущего баланса игрока.
     * <p>
     * В ответе будет текущий баланс авторизованного игрока.
     * </p>
     *
     * @param req  Объект HttpServletRequest, содержащий запрос, сделанный клиентом.
     * @param resp Объект HttpServletResponse, содержащий ответ, отправляемый клиенту.
     * @throws ServletException если запрос не может быть обработан.
     * @throws IOException      при обнаружении ошибки ввода или вывода.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String token = AdminAuthFilter.extractTokenFromHeader(req);

        if (token == null || !JWTUtil.validateToken(token)) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().println("Невалидный токен");
            return;
        }

        try {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            PrintWriter out = resp.getWriter();
            out.println("Текущий баланс: " + ((Player) req.getSession().getAttribute("loggedInPlayer")).getBalance());
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().println("Пользователь не авторизован");
        }
    }
}
