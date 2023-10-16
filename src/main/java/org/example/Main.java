package org.example;

import org.example.in.WalletServiceFacade;
import org.example.in.WalletServiceHandler;
import org.example.interfaces.IAuditRepository;
import org.example.interfaces.IPlayerRepository;
import org.example.interfaces.ITransactionRepository;
import org.example.model.Player;
import org.example.repository.AuditRepository;
import org.example.repository.PlayerRepository;
import org.example.repository.TransactionRepository;
import org.example.service.AuditService;
import org.example.service.PlayerService;
import org.example.service.TransactionService;

public class Main {
    public static void main(String[] args) {
        IPlayerRepository playerRepository = new PlayerRepository();
        ITransactionRepository transactionRepository = new TransactionRepository();
        IAuditRepository auditRepository = new AuditRepository();


        AuditService auditService = new AuditService(auditRepository);
        PlayerService playerService = new PlayerService(playerRepository, auditService);
        TransactionService transactionService = new TransactionService(transactionRepository,
                playerService, auditService);

        playerService.registerPlayer("admin", "admin", Player.Role.ADMIN);

        WalletServiceFacade walletServiceFacade = new WalletServiceFacade(playerService, transactionService, auditService);

        WalletServiceHandler userInputHandler = new WalletServiceHandler(walletServiceFacade);
        userInputHandler.start();
    }
}
