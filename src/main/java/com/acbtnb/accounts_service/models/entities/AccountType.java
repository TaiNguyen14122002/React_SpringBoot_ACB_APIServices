package com.acbtnb.accounts_service.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "account_type")
public class AccountType {

    @Id
//    @NonNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

//    @NonNull
    @Column(length = 10)
    private String name;

    @OneToMany(mappedBy = "account_type")
    @JsonIgnore
    private Set<Account> accounts;
}
