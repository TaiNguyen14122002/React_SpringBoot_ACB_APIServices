package com.acbtnb.accounts_service.models.dtos;

import lombok.Data;

import java.util.List;

@Data
public class BulkAccountTypeDTO {
    private List<AccountTypeDTO> accountTypes;
}
