package com.TaiNguyen.ACBBank.Modal;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class User_Staff_ACBBank {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String employeeCode;

    private String fullName;
    private String email;

    //An Mat Khau
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;


}
