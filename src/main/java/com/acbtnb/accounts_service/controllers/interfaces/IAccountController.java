package com.acbtnb.accounts_service.controllers.interfaces;

import com.acbtnb.accounts_service.models.dtos.AccountDTO;
import com.acbtnb.accounts_service.models.dtos.BulkAccountDTO;
import com.acbtnb.accounts_service.responses.ResponseObject;

public interface IAccountController {
    ResponseObject insertAccount(AccountDTO accountDTO);
    ResponseObject insertBulkAccount(BulkAccountDTO bulkAccountDTO);
    ResponseObject listAccounts();
    ResponseObject getAccount(String id);
    ResponseObject deleteAccount(String id);
}
