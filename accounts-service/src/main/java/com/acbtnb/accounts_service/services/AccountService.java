package com.acbtnb.accounts_service.services;

import com.acbtnb.accounts_service.client.BranchesClient;
import com.acbtnb.accounts_service.client.CustomersClient;
import com.acbtnb.accounts_service.models.converters.AccountDtoConverter;
import com.acbtnb.accounts_service.models.dtos.AccountDTO;
import com.acbtnb.accounts_service.models.dtos.BulkAccountDTO;
import com.acbtnb.accounts_service.models.entities.Account;
import com.acbtnb.accounts_service.repositories.AccountRepository;
import com.acbtnb.accounts_service.responses.ResponseObject;
import com.acbtnb.accounts_service.services.interfaces.IAccountService;
import com.acbtnb.accounts_service.utils.CusResponseMessage;
import com.acbtnb.accounts_service.utils.RandomString;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AccountService implements IAccountService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private BranchesClient branchesClient;
    @Autowired
    private CustomersClient customersClient;

    private final AccountDtoConverter accountDtoConverter = new AccountDtoConverter();

    @Override
    public ResponseObject insertAccount(AccountDTO accountDTO) {

        // Check existed branch
        ResponseObject branchRes = branchesClient.getBranch(accountDTO.getBranch_id());

        if(branchRes.getData() == null)
        {
            return ResponseObject.builder()
                    .status(HttpStatus.NOT_FOUND.name())
                    .message(CusResponseMessage.notFoundBranchMess)
                    .data(null).build();
        }

        // Check existed customer
        ResponseObject customerRes = customersClient.getCustomer(accountDTO.getCustomer_id());

        if(customerRes.getData() == null)
        {
            return ResponseObject.builder()
                    .status(HttpStatus.NOT_FOUND.name())
                    .message(CusResponseMessage.notFoundCustomerMess)
                    .data(null).build();
        }


        // Convert accountDTO to account
        Account account = accountDtoConverter.accountDTOToAccount(accountDTO);

        // Convert customer response data to Json object
        String customerJsonString = new Gson().toJson(customerRes.getData(), Map.class);
        JsonObject customerJson = new Gson().fromJson(customerJsonString, JsonObject.class);


        // Check customer type to random account id or not
        if (!customerJson.get("segment").getAsBoolean())
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

        // Get branch from branches service
        ResponseObject branch = branchesClient.getBranch(account.get().getBranch_id());

        // Get customer from customers service
        ResponseObject customer = customersClient.getCustomer(account.get().getCustomer_id());

        AccountDTO accountDTO = accountDtoConverter.accountToAccountDTO(account.get());
        accountDTO.setBranch(branch.getData());
        accountDTO.setCustomer(customer.getData());

        return ResponseObject.builder()
                .status(HttpStatus.OK.name())
                .message(CusResponseMessage.existedAccountMess)
                .data(accountDTO).build();
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
