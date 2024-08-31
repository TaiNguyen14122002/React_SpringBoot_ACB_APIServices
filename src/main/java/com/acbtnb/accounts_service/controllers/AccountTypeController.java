package com.acbtnb.accounts_service.controllers;

import com.acbtnb.accounts_service.controllers.interfaces.IAccountTypeController;
import com.acbtnb.accounts_service.models.dtos.AccountTypeDTO;
import com.acbtnb.accounts_service.models.dtos.BulkAccountTypeDTO;
import com.acbtnb.accounts_service.responses.ResponseObject;
import com.acbtnb.accounts_service.services.interfaces.IAccountTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account-types")
public class AccountTypeController implements IAccountTypeController {

    @Autowired
    private IAccountTypeService iAccountTypeService;

    @Override
    @PostMapping("/insert-account-type")
    public ResponseObject insertAccountType(@RequestBody AccountTypeDTO accountTypeDTO) {
        return iAccountTypeService.insertAccountType(accountTypeDTO);
    }

    @Override
    @PostMapping("/insert-bulk-account-type")
    public ResponseObject insertBulkAccountType(@RequestBody BulkAccountTypeDTO bulkAccountTypeDTO) {
        return iAccountTypeService.insertBulkAccountType(bulkAccountTypeDTO);
    }

    @Override
    @GetMapping("/list-account-types")
    public ResponseObject listAccountTypes() {
        return iAccountTypeService.listAccountTypes();
    }

    @Override
    @GetMapping("/get-account-type")
    public ResponseObject getAccountType(Integer id) {
        return iAccountTypeService.getAccountType(id);
    }

    @Override
    @DeleteMapping("/delete-account-type")
    public ResponseObject deleteAccountType(Integer id) {
        return iAccountTypeService.deleteAccountType(id);
    }
}
