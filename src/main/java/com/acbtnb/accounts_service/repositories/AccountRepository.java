package com.acbtnb.accounts_service.repositories;

import com.acbtnb.accounts_service.models.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    @Query("SELECT b FROM Account b WHERE b.id = :id AND b.deleted_at IS NULL")
    Optional<Account> findAccountById(Integer id);
}
