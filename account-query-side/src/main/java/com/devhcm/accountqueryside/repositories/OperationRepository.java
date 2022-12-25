package com.devhcm.accountqueryside.repositories;

import com.devhcm.accountqueryside.entities.Account;
import com.devhcm.accountqueryside.entities.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OperationRepository extends JpaRepository<Operation, Long> {
    List<Operation> findOperationsByAccount(Account account);
}