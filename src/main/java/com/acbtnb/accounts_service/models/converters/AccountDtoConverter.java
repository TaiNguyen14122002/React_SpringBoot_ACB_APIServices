package com.acbtnb.accounts_service.models.converters;

import com.acbtnb.accounts_service.models.dtos.AccountDTO;
import com.acbtnb.accounts_service.models.entities.Account;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class AccountDtoConverter {
    @Autowired
    private final ModelMapper modelMapper = new ModelMapper();

    public AccountDtoConverter() {
    }

    public AccountDTO branchToAccountDTO(Account account) {
        return (AccountDTO)this.modelMapper.map(account, AccountDTO.class);
    }

    public Account accountDTOToAccount(AccountDTO accountDTO) {
        return (Account)this.modelMapper.map(accountDTO, Account.class);
    }
}
