package com.acbtnb.accounts_service.models.converters;

import com.acbtnb.accounts_service.models.dtos.AccountDTO;
import com.acbtnb.accounts_service.models.dtos.AccountTypeDTO;
import com.acbtnb.accounts_service.models.entities.Account;
import com.acbtnb.accounts_service.models.entities.AccountType;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountTypeDtoConverter {
    @Autowired
    private final ModelMapper modelMapper = new ModelMapper();
    public AccountTypeDTO accountTypeToAccountTypeDTO(AccountType accountType)
    {
        return modelMapper.map(accountType, AccountTypeDTO.class);
    }

    public AccountType accountTypeDTOToAccountType(AccountTypeDTO accountTypeDTO)
    {
        return modelMapper.map(accountTypeDTO, AccountType.class);
    }
}
