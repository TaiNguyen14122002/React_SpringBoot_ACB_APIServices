package com.acbtnb.accounts_service.services;

import com.acbtnb.accounts_service.models.converters.AccountDtoConverter;
import com.acbtnb.accounts_service.models.converters.AccountTypeDtoConverter;
import com.acbtnb.accounts_service.models.dtos.AccountTypeDTO;
import com.acbtnb.accounts_service.models.dtos.BulkAccountTypeDTO;
import com.acbtnb.accounts_service.models.entities.Account;
import com.acbtnb.accounts_service.models.entities.AccountType;
import com.acbtnb.accounts_service.repositories.AccountRepository;
import com.acbtnb.accounts_service.repositories.AccountTypeRepository;
import com.acbtnb.accounts_service.responses.ResponseObject;
import com.acbtnb.accounts_service.services.interfaces.IAccountTypeService;
import com.acbtnb.accounts_service.utils.CusResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountTypeService implements IAccountTypeService {

    @Autowired
    private AccountTypeRepository accountTypeRepository;
    private final AccountTypeDtoConverter accountTypeDtoConverter = new AccountTypeDtoConverter();
    @Override
    public ResponseObject insertAccountType(AccountTypeDTO accountTypeDTO) {
        //Convert accountTypeDTO to accountType
        AccountType accountType = accountTypeDtoConverter.accountTypeDTOToAccountType(accountTypeDTO);

        accountType = accountTypeRepository.save(accountType);

        return ResponseObject.builder()
                .status(HttpStatus.OK.name())
                .message(CusResponseMessage.insertAccountTypeSuccessMess)
                .data(accountType).build();
    }

    @Override
    public ResponseObject insertBulkAccountType(BulkAccountTypeDTO bulkAccountTypeDTO) {
        List<AccountType> accountTypeList = new ArrayList<>();

        bulkAccountTypeDTO.getAccountTypes().forEach(each -> {
            // Convert accountTypeDTO to accountType
            AccountType accountType = accountTypeDtoConverter.accountTypeDTOToAccountType(each);

            accountTypeList.add(accountType);
        });

        List<AccountType> accountTypeListResult = accountTypeRepository.saveAll(accountTypeList);

        return ResponseObject.builder()
                .status(HttpStatus.OK.name())
                .message(CusResponseMessage.insertBulkAccountTypeSuccessMess)
                .data(accountTypeListResult).build();
    }

    @Override
    public ResponseObject listAccountTypes() {
        List<AccountType> accountTypeList = accountTypeRepository.findAll();


        if (accountTypeList.isEmpty())
        {
            return ResponseObject.builder()
                    .status(HttpStatus.OK.name())
                    .message(CusResponseMessage.emptyAccountTypesMess)
                    .data(null).build();
        }

        return ResponseObject.builder()
                .status(HttpStatus.OK.name())
                .message(CusResponseMessage.existedAccountTypesMess)
                .data(accountTypeList).build();
    }

    @Override
    public ResponseObject getAccountType(Integer id) {
        Optional<AccountType> accountType = accountTypeRepository.findAccountTypeById(id);

        if (accountType.isEmpty())
        {
            return ResponseObject.builder()
                    .status(HttpStatus.OK.name())
                    .message(CusResponseMessage.notFoundAccountTypeMess)
                    .data(null).build();
        }

        return ResponseObject.builder()
                .status(HttpStatus.OK.name())
                .message(CusResponseMessage.existedAccountTypeMess)
                .data(accountType).build();
    }

    @Override
    public ResponseObject deleteAccountType(Integer id) {
        Optional<AccountType> accountType = accountTypeRepository.findAccountTypeById(id);

        if (accountType.isEmpty())
        {
            return ResponseObject.builder()
                    .status(HttpStatus.OK.name())
                    .message(CusResponseMessage.notFoundAccountTypeMess)
                    .data(null).build();
        }

        accountTypeRepository.delete(accountType.get());

        return ResponseObject.builder()
                .status(HttpStatus.OK.name())
                .message(CusResponseMessage.deletedAccountTypeSuccessMess)
                .data(null).build();
    }
}