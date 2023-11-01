package org.example.controller;

import org.example.dto.BalanceDTO;
import org.example.exception.InvalidAmountException;
import org.example.exception.TransactionExistsException;
import org.example.in.WalletServiceFacade;
import org.example.model.Player;
import org.example.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

/**
 * Контроллер для работы с балансом игрока.
 * Позволяет получать текущий баланс и проводить операции по изменению баланса.
 */
@RestController
@RequestMapping("/balances")
public class BalanceController {
    public final JWTUtil jwtUtil;

    public final WalletServiceFacade walletServiceFacade;

    /**
     * Конструктор для {@code BalanceController}.
     *
     * @param jwtUtil инструмент для работы с JWT.
     * @param walletServiceFacade фасад для сервиса кошелька.
     */
    @Autowired
    public BalanceController(JWTUtil jwtUtil, WalletServiceFacade walletServiceFacade) {
        this.jwtUtil = jwtUtil;
        this.walletServiceFacade = walletServiceFacade;
    }

    /**
     * Получение текущего баланса игрока.
     * Метод возвращает текущий баланс пользователя, аутентифицированного через HTTP-сессию.
     *
     * @param bearerToken токен авторизации, полученный в заголовке запроса.
     * @param httpSession текущая сессия HTTP, используется для получения данных авторизованного пользователя.
     * @return {@code ResponseEntity<?>} с текущим балансом игрока, если аутентификация прошла успешно.
     */
    @GetMapping("")
    public ResponseEntity<?> getCurrentBalance(@RequestHeader("Authorization") String bearerToken,
                                               HttpSession httpSession) {

        Player player = (Player) httpSession.getAttribute("loggedPlayer");

        return ResponseEntity.ok(Map.of("message","Текущий баланс: " + player.getBalance()));
    }

    /**
     * Изменение баланса игрока.
     * Метод принимает запрос на изменение баланса (пополнение или снятие) и обрабатывает его,
     * возвращая новый баланс пользователя или сообщение об ошибке.
     *
     * @param balanceDTO DTO, содержащее информацию о транзакции (сумма и действие).
     * @param bearerToken токен авторизации, полученный в заголовке запроса.
     * @param httpSession текущая сессия HTTP, используется для получения данных авторизованного пользователя.
     * @return {@code ResponseEntity<?>} со статусом операции изменения баланса.
     * @throws InvalidAmountException если сумма транзакции недопустима.
     * @throws TransactionExistsException если транзакция уже существует.
     */
    @PostMapping("")
    public ResponseEntity<?> changeBalance(@RequestBody BalanceDTO balanceDTO,
                                           @RequestHeader("Authorization") String bearerToken,
                                           HttpSession httpSession) {

        Player player = (Player) httpSession.getAttribute("loggedPlayer");

        try {
            switch (balanceDTO.getAction()) {
                case "deposit":
                    walletServiceFacade.credit(player, balanceDTO.getAmount(),
                            UUID.fromString(balanceDTO.getTransaction_id()));
                    break;
                case "withdraw":
                    BigDecimal currentBalance = walletServiceFacade.getPlayerBalance(player.getId());
                    if (currentBalance.compareTo(balanceDTO.getAmount()) >= 0) {
                        walletServiceFacade.withdraw(player, balanceDTO.getAmount(),
                                UUID.fromString(balanceDTO.getTransaction_id()));
                    } else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(Map.of("message", "Недостаточно средств"));
                    }
                    break;
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("message", "Текущий баланс: " +
                            walletServiceFacade.getPlayerBalance(player.getId())));
        } catch (InvalidAmountException | TransactionExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Ошибка на сервере"));
        }
    }
}
