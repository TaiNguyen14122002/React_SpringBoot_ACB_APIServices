package com.acbtnb.accounts_service.controllers;

import com.acbtnb.accounts_service.controllers.interfaces.IAccountController;
import com.acbtnb.accounts_service.models.dtos.AccountDTO;
import com.acbtnb.accounts_service.models.dtos.BulkAccountDTO;
import com.acbtnb.accounts_service.responses.ResponseObject;
import com.acbtnb.accounts_service.services.interfaces.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController implements IAccountController {

    @Autowired
    private IAccountService iAccountService;

    @Override
    @PostMapping("/insert-account")
    public ResponseObject insertAccount(@RequestBody AccountDTO accountDTO) {
        return iAccountService.insertAccount(accountDTO);
    }

    @Override
    @PostMapping("/insert-bulk-accounts")
    public ResponseObject insertBulkAccount(@RequestBody BulkAccountDTO bulkAccountDTO) {
        return iAccountService.insertBulkAccounts(bulkAccountDTO);
    }

    @Override
    @GetMapping("/list-accounts")
    public ResponseObject listAccounts() {
        return iAccountService.listAccounts();
    }

    @Override
    @GetMapping("/get-account")
    public ResponseObject getAccount(@RequestParam("id") String id) {
        return iAccountService.getAccount(id);
    }

    @Override
    @PatchMapping("/delete-account")
    public ResponseObject deleteAccount(@RequestParam("id") String id) {
        return iAccountService.deleteAccount(id);
    }
}
