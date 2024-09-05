package com.acbtnb.accounts_service.repositories;

import com.acbtnb.accounts_service.models.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    public Optional<Account> findAccountById(String id);
}
