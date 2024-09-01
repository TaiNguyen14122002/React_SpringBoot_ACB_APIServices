package com.TaiNguyen.ACBBank.Modal;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class ApiLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String method;
    private String requestUri;
    private String request;
    private Integer responseCode;

    @Lob
    @Column(name = "response_body")
    private String responseBody;
    private Long timeTaken;
    private LocalDateTime createdAt;
}
