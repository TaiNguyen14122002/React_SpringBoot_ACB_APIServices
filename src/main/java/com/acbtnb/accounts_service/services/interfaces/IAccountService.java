package com.acbtnb.accounts_service.services.interfaces;

import com.acbtnb.accounts_service.models.dtos.AccountDTO;
import com.acbtnb.accounts_service.responses.ResponseObject;

public interface IAccountService {
        ResponseObject insertAccount(AccountDTO accountDTO);
        ResponseObject deleteAccount(Integer id);
}
