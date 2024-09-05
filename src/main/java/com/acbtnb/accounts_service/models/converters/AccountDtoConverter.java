package com.acbtnb.accounts_service.models.converters;

import com.acbtnb.accounts_service.models.dtos.AccountDTO;
import com.acbtnb.accounts_service.models.entities.Account;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountDtoConverter {
    @Autowired
    private final ModelMapper modelMapper = new ModelMapper();
    public AccountDTO accountToAccountDTO(Account account)
    {
        return modelMapper.map(account, AccountDTO.class);
    }

    public Account accountDTOToAccount(AccountDTO accountDTO)
    {
        return modelMapper.map(accountDTO, Account.class);
    }
}
