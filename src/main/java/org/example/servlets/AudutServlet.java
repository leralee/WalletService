package org.example.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.model.Audit;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Сервлет для обработки аудита транзакций.
 * <p>
 * Данный сервлет предоставляет точку доступа для получения списка аудита всех транзакций.
 * </p>
 */
@WebServlet("/admin/audits")
public class AudutServlet extends BaseServlet {
    private final ObjectMapper objectMapper;

    /**
     * Конструктор для инициализации объекта для преобразования объектов в JSON.
     */
    public AudutServlet() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Обрабатывает GET-запросы для получения списка аудита транзакций.
     * <p>
     * В ответе будет JSON-представление списка аудита транзакций.
     * </p>
     *
     * @param req  Объект HttpServletRequest, содержащий запрос, сделанный клиентом.
     * @param resp Объект HttpServletResponse, содержащий ответ, отправляемый клиенту.
     * @throws IOException при обнаружении ошибки ввода или вывода.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        List<Audit> auditList = walletServiceFacade.getRecords();
        objectMapper.registerModule(new JavaTimeModule());

        String jsonResponse = objectMapper.writeValueAsString(auditList);
        resp.getWriter().write(jsonResponse);
    }
}
