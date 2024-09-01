package com.TaiNguyen.ACBBank.Modal;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class User_Staff_ACBBank {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String employeeCode;

    @Column(nullable = false)
    private String fullName;
    @Column(nullable = false)
    private String email;

    //An Mat Khau
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String password;


}
