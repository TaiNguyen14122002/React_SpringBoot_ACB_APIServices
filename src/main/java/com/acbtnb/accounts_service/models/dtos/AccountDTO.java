package com.acbtnb.accounts_service.models.dtos;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
    private String account_number;
    private Integer branch_id;
    private Integer account_type_id;
}
