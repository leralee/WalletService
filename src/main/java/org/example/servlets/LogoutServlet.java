package org.example.servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Сервлет, обрабатывающий выход пользователя из системы.
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    /**
     * Обрабатывает POST-запрос на выход из системы.
     * Если сессия существует, она аннулируется, и пользователь считается вышедшим из системы.
     *
     * @param req  HttpServletRequest, представляющий запрос клиента к сервлету.
     * @param resp HttpServletResponse, представляющий ответ, который сервлет отправляет клиенту.
     * @throws IOException В случае ошибок ввода-вывода при записи в поток ответа.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println("Вы успешно вышли из системы");
    }
}
