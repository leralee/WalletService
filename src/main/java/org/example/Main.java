package org.example;

import org.example.in.UserInputHandler;
import org.example.model.Player;
import org.example.repository.AuditRepository;
import org.example.repository.PlayerRepository;
import org.example.repository.TransactionRepository;
import org.example.service.AuditService;
import org.example.service.PlayerService;
import org.example.service.TransactionService;

import java.util.UUID;

/**
 * Hello world!
 *
 */
public class Main
{
    public static void main( String[] args )
    {
        PlayerRepository playerRepository = new PlayerRepository();
        TransactionRepository transactionRepository = new TransactionRepository();
        AuditRepository auditRepository = new AuditRepository();


        AuditService auditService = new AuditService(auditRepository);
        PlayerService playerService = new PlayerService(playerRepository, auditService);
        TransactionService transactionService = new TransactionService(transactionRepository,
                playerService, auditService);

        playerService.registerPlayer("admin", "admin", Player.Role.ADMIN);

        UserInputHandler userInputHandler = new UserInputHandler(playerService, transactionService, auditService);
        userInputHandler.start();
    }
}
