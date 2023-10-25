package org.example.servlets;


import org.example.in.WalletServiceFacade;
import org.example.interfaces.IAuditRepository;
import org.example.interfaces.IPlayerRepository;
import org.example.interfaces.ITransactionRepository;
import org.example.repository.AuditRepository;
import org.example.repository.PlayerRepository;
import org.example.repository.TransactionRepository;
import org.example.service.AuditService;
import org.example.service.PlayerService;
import org.example.service.TransactionService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * Базовый сервлет, предоставляющий общую логику инициализации для всех дочерних сервлетов.
 * <p>
 * Данный сервлет инициализирует репозитории, сервисы и фасад для доступа к функциональности кошелька.
 * </p>
 */
public abstract class BaseServlet extends HttpServlet {

    protected WalletServiceFacade walletServiceFacade;

    /**
     * Инициализация сервлета с созданием всех необходимых репозиториев, сервисов и фасада.
     * <p>
     * Метод вызывается при первом создании сервлета в контейнере сервлетов.
     * </p>
     */
    @Override
    public void init() {
        IPlayerRepository playerRepository = new PlayerRepository();
        ITransactionRepository transactionRepository = new TransactionRepository();
        IAuditRepository auditRepository = new AuditRepository();

        AuditService auditService = new AuditService(auditRepository);
        PlayerService playerService = new PlayerService(playerRepository, auditService);
        TransactionService transactionService = new TransactionService(transactionRepository, playerService, auditService);

        this.walletServiceFacade = new WalletServiceFacade(playerService, transactionService, auditService);
    }
}
