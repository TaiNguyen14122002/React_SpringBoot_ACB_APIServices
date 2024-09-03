package com.acbtnb.accounts_service.controllers;

import com.acbtnb.accounts_service.models.dtos.AccountDTO;
import com.acbtnb.accounts_service.responses.ResponseObject;
import com.acbtnb.accounts_service.services.interfaces.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/accounts"})
public class AccountController {
    @Autowired
    private IAccountService iAccountService;

    public AccountController() {
    }

    @PostMapping
    public ResponseObject insertAccount(@RequestBody AccountDTO accountDTO) {
        return this.iAccountService.insertAccount(accountDTO);
    }

    @PutMapping({"/{id}"})
    public ResponseObject updateAccount(@PathVariable Integer id, @RequestBody AccountDTO accountDTO) {
        return this.iAccountService.updateAccount(id, accountDTO);
    }

    @DeleteMapping({"/{id}"})
    public ResponseObject deleteAccount(@PathVariable Integer id) {
        return this.iAccountService.deleteAccount(id);
    }

}
