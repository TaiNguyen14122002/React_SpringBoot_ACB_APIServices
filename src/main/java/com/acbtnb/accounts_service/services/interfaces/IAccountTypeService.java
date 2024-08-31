package com.acbtnb.accounts_service.services.interfaces;

import com.acbtnb.accounts_service.models.dtos.AccountTypeDTO;
import com.acbtnb.accounts_service.models.dtos.BulkAccountTypeDTO;
import com.acbtnb.accounts_service.responses.ResponseObject;

public interface IAccountTypeService {
    ResponseObject insertAccountType(AccountTypeDTO accountTypeDTO);
    ResponseObject insertBulkAccountType(BulkAccountTypeDTO bulkAccountTypeDTO);
    ResponseObject listAccountTypes();
    ResponseObject getAccountType(Integer id);
    ResponseObject deleteAccountType(Integer id);
}
