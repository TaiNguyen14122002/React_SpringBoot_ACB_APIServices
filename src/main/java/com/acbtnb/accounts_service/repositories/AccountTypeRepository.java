package com.acbtnb.accounts_service.repositories;

import com.acbtnb.accounts_service.models.entities.AccountType;
import org.springframework.data.repository.CrudRepository;

public interface AccountTypeRepository extends CrudRepository<AccountType, Integer> {
}