package com.acbtnb.accounts_service.models.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "account")
public class Account {

    @Id
    @NonNull
    @Column(length = 10)
    private String id;

    @NonNull
    private Integer customer_id;

    @NonNull
    private Integer branch_id;

    @NonNull
    private BigInteger balance;

    @NonNull
    private String status;

    @NonNull
    private String currency;

    @NonNull
    @Column(name = "created_at")
    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
    private LocalDateTime created_at = LocalDateTime.now();

    @Column(name = "deleted_at")
    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
    private LocalDateTime deleted_at;

    @ManyToOne
    @JoinColumn(name = "account_type_id", nullable = false)
    private AccountType account_type;
}
