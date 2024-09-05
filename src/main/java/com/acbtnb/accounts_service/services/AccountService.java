package com.acbtnb.accounts_service.services;

import com.acbtnb.accounts_service.models.converters.AccountDtoConverter;
import com.acbtnb.accounts_service.models.dtos.AccountDTO;
import com.acbtnb.accounts_service.models.dtos.BulkAccountDTO;
import com.acbtnb.accounts_service.models.entities.Account;
import com.acbtnb.accounts_service.models.entities.AccountType;
import com.acbtnb.accounts_service.repositories.AccountRepository;
import com.acbtnb.accounts_service.repositories.AccountTypeRepository;
import com.acbtnb.accounts_service.responses.ResponseObject;
import com.acbtnb.accounts_service.services.interfaces.IAccountService;
import com.acbtnb.accounts_service.utils.CusResponseMessage;
import com.acbtnb.accounts_service.utils.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService implements IAccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountTypeRepository accountTypeRepository;

    private final AccountDtoConverter accountDtoConverter = new AccountDtoConverter();

    @Override
    public ResponseObject insertAccount(AccountDTO accountDTO) {

        // Check existed account type
        Optional<AccountType> accountType = accountTypeRepository.findAccountTypeById(accountDTO.getAccount_type_id());

        if(accountType.isEmpty())
        {
            return ResponseObject.builder()
                    .status(HttpStatus.OK.name())
                    .message(CusResponseMessage.notFoundAccountTypeMess)
                    .data(null).build();
        }

        //Convert accountDTO to account
        Account account = accountDtoConverter.accountDTOToAccount(accountDTO);

        // Set account type for this account
        account.setAccount_type(accountType.get());

        // Check account type to random account id or not
        if (!accountType.get().getName().equals("vip"))
        {
            // Random id
            String randomId = RandomString.getNumericString(10);

            account.setId(randomId);
        }

        account = accountRepository.save(account);

        return ResponseObject.builder()
                .status(HttpStatus.OK.name())
                .message(CusResponseMessage.insertAccountSuccessMess)
                .data(account).build();
    }

    @Override
    public ResponseObject insertBulkAccounts(BulkAccountDTO bulkAccountDTO) {
        List<Account> accountList = new ArrayList<>();

        bulkAccountDTO.getAccounts().forEach(each -> {
            //Convert accountDTO to account
            Account account = accountDtoConverter.accountDTOToAccount(each);

            accountList.add(account);
        });

        List<Account> accountListResult = accountRepository.saveAll(accountList);

        return ResponseObject.builder()
                .status(HttpStatus.OK.name())
                .message(CusResponseMessage.insertBulkAccountSuccessMess)
                .data(accountListResult).build();
    }

    @Override
    public ResponseObject getAccount(String id) {
        Optional<Account> account = accountRepository.findAccountById(id);

        if (account.isEmpty() || account.get().getDeleted_at() != null)
        {
            return ResponseObject.builder()
                    .status(HttpStatus.OK.name())
                    .message(CusResponseMessage.notFoundAccountMess)
                    .data(null).build();
        }

        return ResponseObject.builder()
                .status(HttpStatus.OK.name())
                .message(CusResponseMessage.existedAccountMess)
                .data(account).build();
    }

    @Override
    public ResponseObject listAccounts() {
        List<Account> accountList = accountRepository.findAll();

        List<Account> resAccountList = accountList.stream()
                .filter(e -> e.getDeleted_at() == null)
                .toList();

        if (resAccountList.isEmpty())
        {
            return ResponseObject.builder()
                    .status(HttpStatus.OK.name())
                    .message(CusResponseMessage.emptyAccountsMess)
                    .data(null).build();
        }

        return ResponseObject.builder()
                .status(HttpStatus.OK.name())
                .message(CusResponseMessage.existedAccountsMess)
                .data(resAccountList).build();
    }

    @Override
    public ResponseObject deleteAccount(String id) {
        Optional<Account> account = accountRepository.findAccountById(id);

        if (account.isEmpty())
        {
            return ResponseObject.builder()
                    .status(HttpStatus.OK.name())
                    .message(CusResponseMessage.notFoundAccountMess)
                    .data(null).build();
        }

        account.get().setDeleted_at(LocalDateTime.now());

        accountRepository.save(account.get());

        return ResponseObject.builder()
                .status(HttpStatus.OK.name())
                .message(CusResponseMessage.deletedAccountSuccessMess)
                .data(account).build();
    }
}
