package com.acbtnb.accounts_service.services;

import com.acbtnb.accounts_service.models.converters.AccountDtoConverter;
import com.acbtnb.accounts_service.models.dtos.AccountDTO;
import com.acbtnb.accounts_service.models.entities.Account;
import com.acbtnb.accounts_service.repositories.AccountRepository;
import com.acbtnb.accounts_service.responses.ResponseObject;
import com.acbtnb.accounts_service.services.interfaces.IAccountService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AccountService implements IAccountService {
    @Autowired
    private AccountRepository accountRepository;
    private final AccountDtoConverter accountDtoConverter = new AccountDtoConverter();
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private ModelMapper modelMapper;

    public AccountService() {
    }

    public ResponseObject insertAccount(AccountDTO branchDTO) {
        Account branch = this.accountDtoConverter.accountDTOToAccount(branchDTO);

        try {
            Account newAccount = (Account)this.accountRepository.save(branch);
            return ResponseObject.builder().status(HttpStatus.OK.name()).message("Insert new branch successfully").data(newAccount).build();
        } catch (Exception var4) {
            return ResponseObject.builder().status(HttpStatus.INTERNAL_SERVER_ERROR.name()).message("Insert new branch failed").build();
        }
    }

    public ResponseObject updateAccount(Integer id, AccountDTO accountDTO) {
        Optional<Account> checkAccount = this.accountRepository.findAccountById(id);
        if (checkAccount.isEmpty()) {
            return ResponseObject.builder().status(HttpStatus.BAD_REQUEST.name()).message("Account doesn't exist").build();
        } else {
            Account account = this.accountDtoConverter.accountDTOToAccount(accountDTO);
            account.setId(id);

            try {
                Account updatedAccount = (Account)this.accountRepository.save(account);
                return ResponseObject.builder().status(HttpStatus.OK.name()).message("Update branch successfully").data(updatedAccount).build();
            } catch (Exception var6) {
                return ResponseObject.builder().status(HttpStatus.INTERNAL_SERVER_ERROR.name()).message("Update branch failed").build();
            }
        }
    }

    public ResponseObject deleteAccount(Integer id) {
        Optional<Account> accountOptional = this.accountRepository.findAccountById(id);
        if (accountOptional.isPresent()) {
            Account account= (Account)accountOptional.get();
            account.setId(id);
            account.setDeleted_at(LocalDateTime.now());

            try {
                this.accountRepository.save(account);
                return ResponseObject.builder().status(HttpStatus.OK.name()).message("Delete branch successfully").build();
            } catch (Exception var5) {
                return ResponseObject.builder().status(HttpStatus.INTERNAL_SERVER_ERROR.name()).message("Delete branch failed").build();
            }
        } else {
            return ResponseObject.builder().status(HttpStatus.NOT_FOUND.name()).message("Account doesn't exist").build();
        }
    }
}
