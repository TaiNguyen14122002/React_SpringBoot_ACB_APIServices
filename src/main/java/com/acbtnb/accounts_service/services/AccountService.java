package com.acbtnb.accounts_service.services;

import com.acbtnb.accounts_service.models.converters.AccountDtoConverter;
import com.acbtnb.accounts_service.models.dtos.AccountDTO;
import com.acbtnb.accounts_service.models.entities.Account;
import com.acbtnb.accounts_service.models.entities.AccountType;
import com.acbtnb.accounts_service.repositories.AccountTypeRepository;
import com.acbtnb.accounts_service.repositories.AccountRepository;
import com.acbtnb.accounts_service.responses.ResponseObject;
import com.acbtnb.accounts_service.services.interfaces.IAccountService;
import com.acbtnb.accounts_service.utils.CusResponseMessage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
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
    private AccountTypeRepository accountTypeRepository;

    public AccountService() {

    }

    public ResponseObject insertAccount(AccountDTO accountDTO) {
        Optional<AccountType> accountType = accountTypeRepository.findById(accountDTO.getAccount_type_id());

        if (accountType.isEmpty()) {
            return ResponseObject.builder()
                    .status(HttpStatus.OK.name())
                    .message("Account Type is invalid.")
                    .data(null).build();
        }

        Account account = this.accountDtoConverter.accountDTOToAccount(accountDTO);

        account.setAccount_type(accountType.get());

        if (!accountType.get().getName().equals("VIP")) {
            //thuong
            account.setAccount_number(String.valueOf(Math.random() * 10));
        } else {
            account.setAccount_number(accountDTO.getAccount_number());
        }

        try {
            Account newAccount = this.accountRepository.save(account);
            return ResponseObject.builder().status(HttpStatus.OK.name()).message("Insert new account successfully").data(newAccount).build();
        } catch (Exception var4) {
            return ResponseObject.builder().status(HttpStatus.INTERNAL_SERVER_ERROR.name()).message("Insert new account failed").build();
        }
    }


    public ResponseObject deleteAccount(Integer id) {
        Optional<Account> accountOptional = this.accountRepository.findAccountById(id);
        try {


            if (accountOptional.isPresent()) {
                Account account = accountOptional.get();

                account.setDeleted_at(LocalDateTime.now());

                accountRepository.save(account);

                return ResponseObject.builder().status(HttpStatus.OK.name()).message("Delete account successfully").build();

            } else {
                return ResponseObject.builder().status(HttpStatus.NOT_FOUND.name()).message("Account doesn't exist").build();
            }
        } catch (Exception var5) {
            return ResponseObject.builder().status(HttpStatus.INTERNAL_SERVER_ERROR.name()).message("Delete account failed").build();
        }
    }
}
