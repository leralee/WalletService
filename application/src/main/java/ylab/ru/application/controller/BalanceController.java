//package org.example.controller;
//
//import jakarta.servlet.http.HttpServletRequest;
//import org.example.common.Player;
//import org.example.dto.BalanceDTO;
//import org.example.exception.InvalidAmountException;
//import org.example.exception.TransactionExistsException;
//import org.example.in.WalletServiceFacade;
//
//import org.example.security.JWTUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.math.BigDecimal;
//import java.util.Map;
//import java.util.UUID;
//
///**
// * Контроллер для работы с балансом игрока.
// * Позволяет получать текущий баланс и проводить операции по изменению баланса.
// */
//@RestController
//@RequestMapping("/balances")
//public class BalanceController {
//    public final JWTUtil jwtUtil;
//
//    public final WalletServiceFacade walletServiceFacade;
//
//    /**
//     * Конструктор для {@code BalanceController}.
//     *
//     * @param jwtUtil инструмент для работы с JWT.
//     * @param walletServiceFacade фасад для сервиса кошелька.
//     */
//    @Autowired
//    public BalanceController(JWTUtil jwtUtil, WalletServiceFacade walletServiceFacade) {
//        this.jwtUtil = jwtUtil;
//        this.walletServiceFacade = walletServiceFacade;
//    }
//
//
//    @GetMapping("")
//    public ResponseEntity<?> getCurrentBalance(HttpServletRequest request) {
//
//        Player player = (Player) request.getAttribute("player");
//
//        return ResponseEntity.ok(Map.of("message","Текущий баланс: " + player.getBalance()));
//    }
//
//    /**
//     * Изменение баланса игрока.
//     * Метод принимает запрос на изменение баланса (пополнение или снятие) и обрабатывает его,
//     * возвращая новый баланс пользователя или сообщение об ошибке.
//     *
//     * @param balanceDTO DTO, содержащее информацию о транзакции (сумма и действие).
//     * @return {@code ResponseEntity<?>} со статусом операции изменения баланса.
//     */
//    @PostMapping("/deposit")
//    public ResponseEntity<?> deposit(@RequestBody BalanceDTO balanceDTO, HttpServletRequest request) {
//        Player player = (Player) request.getAttribute("player");
//        if (player == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body(Map.of("message", "Пользователь не авторизован"));
//        }
//        try {
//            walletServiceFacade.credit(player, balanceDTO.getAmount(), UUID.fromString(balanceDTO.getTransaction_id()));
//            return ResponseEntity.ok(Map.of("message", "Средства успешно зачислены"));
//        } catch (InvalidAmountException | TransactionExistsException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(Map.of("message", e.getMessage()));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Map.of("message", "Ошибка на сервере"));
//        }
//    }
//
//    @PostMapping("/withdraw")
//    public ResponseEntity<?> withdraw(@RequestBody BalanceDTO balanceDTO, HttpServletRequest request) {
//        Player player = (Player) request.getAttribute("player");
//        if (player == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body(Map.of("message", "Пользователь не авторизован"));
//        }
//
//        try {
//            BigDecimal currentBalance = walletServiceFacade.getPlayerBalance(player.getId());
//            if (currentBalance.compareTo(balanceDTO.getAmount()) >= 0) {
//                walletServiceFacade.withdraw(player, balanceDTO.getAmount(), UUID.fromString(balanceDTO.getTransaction_id()));
//                return ResponseEntity.ok(Map.of("message", "Средства успешно сняты"));
//            } else {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                        .body(Map.of("message", "Недостаточно средств"));
//            }
//        } catch (InvalidAmountException | TransactionExistsException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(Map.of("message", e.getMessage()));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Map.of("message", "Ошибка на сервере"));
//        }
//    }
//}
