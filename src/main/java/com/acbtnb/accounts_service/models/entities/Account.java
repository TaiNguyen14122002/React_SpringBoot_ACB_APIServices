package com.acbtnb.accounts_service.models.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String account_number;

//    @NonNull
    private Integer customer_id;

//    @NonNull
    private Integer branch_id;

//    @NonNull
    private String status;


//    @NonNull
    @Column(name = "created_at")
    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
    private LocalDateTime created_at = LocalDateTime.now();

    @Column(name = "deleted_at")
    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
    private LocalDateTime deleted_at;

    @ManyToOne
    @JoinColumn(name = "account_type_id")
    @JsonIgnore
    private AccountType account_type;
}

