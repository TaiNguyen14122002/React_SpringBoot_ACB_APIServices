package com.acbtnb.accounts_service.models.dtos;

import lombok.Data;

import java.util.List;

@Data
public class BulkAccountDTO {
    private List<AccountDTO> accounts;
}
