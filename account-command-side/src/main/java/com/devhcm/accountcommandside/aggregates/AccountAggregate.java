package com.devhcm.accountcommandside.aggregates;

import com.hachmi.commonapi.commands.CreateAccountCommand;
import com.hachmi.commonapi.commands.CreditAccountCommand;
import com.hachmi.commonapi.commands.DebitAccountCommand;
import com.hachmi.commonapi.enums.AccountStatus;
import com.hachmi.commonapi.events.AccountActivatedEvent;
import com.hachmi.commonapi.events.AccountCreatedEvent;
import com.hachmi.commonapi.events.AccountCreditedEvent;
import com.hachmi.commonapi.events.AccountDebitedEvent;
import com.hachmi.commonapi.exceptions.AmountNegativeException;
import com.hachmi.commonapi.exceptions.BalanceNotSufficientException;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class AccountAggregate {
    @AggregateIdentifier
    private String accountId;
    private double balance;
    private String currency;
    private AccountStatus status;

    public AccountAggregate() {
        // Empty constructor required by AXON
    }

    @CommandHandler
    public AccountAggregate(CreateAccountCommand createAccountCommand) {
        if(createAccountCommand.getInitialBalance()<0)
            throw new RuntimeException("Impossible !");
        AggregateLifecycle.apply(new AccountCreatedEvent(
                createAccountCommand.getId(),
                createAccountCommand.getInitialBalance(),
                createAccountCommand.getCurrency(),
                AccountStatus.CREATED));
    }

    @EventSourcingHandler
    public void on(AccountCreatedEvent event) {
        this.accountId = event.getId();
        this.balance = event.getInitialBalance();
        this.currency = event.getCurrency();
        this.status = AccountStatus.CREATED;
        AggregateLifecycle.apply(new AccountActivatedEvent(
            event.getId(),
            AccountStatus.ACTIVATED
        ));
    }

    @EventSourcingHandler
    public void on(AccountActivatedEvent event) {
        this.status = event.getStatus();
    }


    @CommandHandler
    public void handle(CreditAccountCommand creditAccountCommand) {
        if(creditAccountCommand.getAmount()<=0)
            throw new AmountNegativeException("Amount should not be negative !");
        AggregateLifecycle.apply(new AccountCreditedEvent(
                creditAccountCommand.getId(),
                creditAccountCommand.getAmount(),
                creditAccountCommand.getCurrency()
        ));
    }

    @EventSourcingHandler
    public void on(AccountCreditedEvent event) {
        this.balance += event.getAmount();
    }

    @CommandHandler
    public void handle(DebitAccountCommand debitAccountCommand) {
        if(debitAccountCommand.getAmount() < 0)
            throw new AmountNegativeException("Amount should not be negative !");
        if(this.balance < debitAccountCommand.getAmount())
            throw new BalanceNotSufficientException("Balance insufficient = " + balance);
        AggregateLifecycle.apply(new AccountDebitedEvent(
                debitAccountCommand.getId(),
                debitAccountCommand.getAmount(),
                debitAccountCommand.getCurrency()
        ));
    }

    @EventSourcingHandler
    public void on(AccountDebitedEvent event) {
        this.balance -= event.getAmount();
    }
}
