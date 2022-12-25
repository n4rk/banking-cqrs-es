package com.devhcm.accountqueryside.repositories;

import com.devhcm.accountqueryside.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
}