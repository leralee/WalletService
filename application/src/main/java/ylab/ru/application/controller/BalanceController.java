package ylab.ru.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ylab.common.exception.InvalidAmountException;
import ru.ylab.common.exception.TransactionExistsException;
import ru.ylab.common.model.Player;
import ru.ylab.common.service.PlayerService;
import ru.ylab.common.service.TransactionService;
import ylab.ru.application.dto.BalanceDTO;
import ylab.ru.application.security.JWTUtil;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

/**
 * Контроллер для работы с балансом игрока.
 * Позволяет получать текущий баланс и проводить операции по изменению баланса.
 */
@Tag(name = "Balance Controller", description = "Контроллер для операций ввода/вывода средств")
@RestController
@RequestMapping("/balances")
public class BalanceController {
    public final JWTUtil jwtUtil;
    private final TransactionService transactionService;
    private final PlayerService playerService;

    /**
     * Конструктор для {@code BalanceController}.
     *
     * @param jwtUtil
     * @param transactionService
     * @param playerService
     */
    @Autowired
    public BalanceController(JWTUtil jwtUtil, TransactionService transactionService, PlayerService playerService) {
        this.jwtUtil = jwtUtil;
        this.transactionService = transactionService;
        this.playerService = playerService;
    }

    @Operation(summary = "Получение текущего баланса игрока",
            description = "Возвращает сообщение с текущим балансом авторизованного игрока.",
            security = @SecurityRequirement(name = "Bearer"))
    @ApiResponse(responseCode = "200", description = "Успешное получение баланса")
    @GetMapping("")
    public ResponseEntity<?> getCurrentBalance(HttpServletRequest request) {

        Player player = (Player) request.getAttribute("player");

        return ResponseEntity.ok(Map.of("message","Текущий баланс: " + player.getBalance()));
    }

    @Operation(
            summary = "Зачисление средств на счет игрока",
            description = "Зачисляет средства на счет игрока на основе данных, предоставленных в теле запроса.",
            security = @SecurityRequirement(name = "Bearer")
    )
    @ApiResponse(responseCode = "200", description = "Средства успешно зачислены")
    @ApiResponse(responseCode = "400", description = "Неверный запрос или данные транзакции")
    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody BalanceDTO balanceDTO, HttpServletRequest request) {
        Player player = (Player) request.getAttribute("player");
        if (player == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Пользователь не авторизован"));
        }
        try {
            transactionService.credit(player, balanceDTO.getAmount(), UUID.fromString(balanceDTO.getTransaction_id()));
            return ResponseEntity.ok(Map.of("message", "Средства успешно зачислены"));
        } catch (InvalidAmountException | TransactionExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Ошибка на сервере"));
        }
    }

    @Operation(
            summary = "Снятие средств со счета игрока",
            description = "Снимает указанную сумму со счета игрока, если на счету достаточно средств.",
            security = @SecurityRequirement(name = "Bearer")
    )
    @ApiResponse(responseCode = "200", description = "Средства успешно сняты")
    @ApiResponse(responseCode = "400", description = "Недостаточно средств или ошибка в запросе")
    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
    @ApiResponse(responseCode = "500", description = "Ошибка на сервере")
    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestBody BalanceDTO balanceDTO, HttpServletRequest request) {
        Player player = (Player) request.getAttribute("player");
        if (player == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Пользователь не авторизован"));
        }

        try {
            BigDecimal currentBalance = playerService.getPlayerBalance(player.getId());
            if (currentBalance.compareTo(balanceDTO.getAmount()) >= 0) {
                transactionService.withdraw(player, balanceDTO.getAmount(), UUID.fromString(balanceDTO.getTransaction_id()));
                return ResponseEntity.ok(Map.of("message", "Средства успешно сняты"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Недостаточно средств"));
            }
        } catch (InvalidAmountException | TransactionExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Ошибка на сервере"));
        }
    }
}
