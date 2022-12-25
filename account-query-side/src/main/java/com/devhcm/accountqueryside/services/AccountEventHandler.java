package com.devhcm.accountqueryside.services;

import com.devhcm.accountqueryside.repositories.AccountRepository;
import com.devhcm.accountqueryside.entities.Account;
import com.devhcm.accountqueryside.entities.Operation;
import com.devhcm.accountqueryside.repositories.OperationRepository;
import com.hachmi.commonapi.enums.AccountStatus;
import com.hachmi.commonapi.enums.OperationType;
import com.hachmi.commonapi.events.AccountActivatedEvent;
import com.hachmi.commonapi.events.AccountCreatedEvent;
import com.hachmi.commonapi.events.AccountCreditedEvent;
import com.hachmi.commonapi.events.AccountDebitedEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class AccountEventHandler {
    private AccountRepository accountRepository;
    private OperationRepository operationRepository;

    @EventHandler
    public void on(AccountCreatedEvent event) {
        log.info("********************************");
        log.info("AccountCreatedEvent Received...");
        Account account = new Account();
        account.setId(event.getId());
        account.setBalance(event.getInitialBalance());
        account.setStatus(event.getStatus());
        account.setCurrency(event.getCurrency());
        accountRepository.save(account);
    }
    @EventHandler
    public void on(AccountActivatedEvent event) {
        log.info("********************************");
        log.info("AccountActivatedEvent Received...");
        Account account = accountRepository.findById(event.getId()).get();
        account.setStatus(AccountStatus.ACTIVATED);
        accountRepository.save(account);
    }
    @EventHandler
    public void on(AccountDebitedEvent event) {
        log.info("********************************");
        log.info("AccountDebitedEvent Received...");
        Account account = accountRepository.findById(event.getId()).get();
        Operation debit = new Operation();
        debit.setAmount(event.getAmount());
        debit.setType(OperationType.DEBIT);
        debit.setDate(new Date()); // À ne pas faire !
        debit.setAccount(account);
        operationRepository.save(debit);
        account.setBalance(account.getBalance() - event.getAmount());
        accountRepository.save(account);
    }
    @EventHandler
    public void on(AccountCreditedEvent event) {
        log.info("********************************");
        log.info("AccountCreditedEvent Received...");
        Account account = accountRepository.findById(event.getId()).get();
        Operation credit = new Operation();
        credit.setAmount(event.getAmount());
        credit.setType(OperationType.CREDIT);
        credit.setDate(new Date()); // À ne pas faire !
        credit.setAccount(account);
        operationRepository.save(credit);
        account.setBalance(account.getBalance() + event.getAmount());
        accountRepository.save(account);
    }

}
