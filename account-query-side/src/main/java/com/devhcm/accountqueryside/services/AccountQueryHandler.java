package com.devhcm.accountqueryside.services;

import com.devhcm.accountqueryside.entities.Account;
import com.devhcm.accountqueryside.entities.Operation;
import com.devhcm.accountqueryside.repositories.AccountRepository;
import com.devhcm.accountqueryside.repositories.OperationRepository;
import com.hachmi.commonapi.query.GetAccountByIdQuery;
import com.hachmi.commonapi.query.GetAllAccountsQuery;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class AccountQueryHandler {
    private AccountRepository accountRepository;
    private OperationRepository operationRepository;

    @QueryHandler
    public List<Account> on(GetAllAccountsQuery query) {
        log.info("--------------------------------");
        log.info("GetAllAccountsQuery Received...");
        List<Account> list = accountRepository.findAll();
        for(Account a:list) {
            List<Operation> listOp = operationRepository.findOperationsByAccount(a);
            a.setOperations(listOp);
        }
        return list;
    }

    @QueryHandler
    public Account on(GetAccountByIdQuery query) {
        log.info("--------------------------------");
        log.info("GetAccountByIdQuery Received...");
        Account a = accountRepository.findById(query.getId()).get();
        List<Operation> listOp = operationRepository.findOperationsByAccount(a);
        a.setOperations(listOp);
        return a;
    }

}
