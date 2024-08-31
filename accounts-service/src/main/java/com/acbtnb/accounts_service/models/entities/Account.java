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
    private Integer branch_id;

    @NonNull
    @Column(length = 12)
    private String customer_id;


    @NonNull
    private String status;

    @NonNull
    @Column(name = "created_at")
    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
    private LocalDateTime created_at = LocalDateTime.now();

    @Column(name = "deleted_at")
    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
    private LocalDateTime deleted_at;

}
