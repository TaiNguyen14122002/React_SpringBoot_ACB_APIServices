package com.acbtnb.accounts_service.repositories;

import com.acbtnb.accounts_service.models.entities.Account;
import com.acbtnb.accounts_service.models.entities.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountTypeRepository extends JpaRepository<AccountType, Integer> {
    public Optional<AccountType> findAccountTypeById(Integer id);
}
