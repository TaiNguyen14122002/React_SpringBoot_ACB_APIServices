package com.acbtnb.accounts_service.models.dtos;
import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {

    private String id;

    private Integer branch_id;

    private Integer customer_id;

    private String status;

    private Integer account_type_id;
}
