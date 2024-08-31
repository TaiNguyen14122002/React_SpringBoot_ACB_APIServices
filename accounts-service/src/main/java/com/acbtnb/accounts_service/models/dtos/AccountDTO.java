package com.acbtnb.accounts_service.models.dtos;
import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {

    private String id;

    private Integer branch_id;

    private String customer_id;

    private String status;

    private Object branch;

    private Object customer;
}
