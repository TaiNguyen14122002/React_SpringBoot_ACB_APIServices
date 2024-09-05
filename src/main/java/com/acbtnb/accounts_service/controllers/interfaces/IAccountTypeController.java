package com.acbtnb.accounts_service.controllers.interfaces;

import com.acbtnb.accounts_service.models.dtos.AccountDTO;
import com.acbtnb.accounts_service.models.dtos.AccountTypeDTO;
import com.acbtnb.accounts_service.models.dtos.BulkAccountDTO;
import com.acbtnb.accounts_service.models.dtos.BulkAccountTypeDTO;
import com.acbtnb.accounts_service.responses.ResponseObject;

public interface IAccountTypeController {
    ResponseObject insertAccountType(AccountTypeDTO accountTypeDTO);
    ResponseObject insertBulkAccountType(BulkAccountTypeDTO bulkAccountTypeDTO);
    ResponseObject listAccountTypes();
    ResponseObject getAccountType(Integer id);
    ResponseObject deleteAccountType(Integer id);
}
