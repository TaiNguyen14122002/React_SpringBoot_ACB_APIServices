package com.CustomerService.CustomerService.Model.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Customers")
public class Customers {

    @Id
    @Column(unique = true)
    private String customerId;

    private LocalDate issuedOn; // Ngày phát hành
    private String issuedAt; // Nơi phát hành
    private String frontImage; // Đường dẫn URL của ảnh mặt trước
    private String backImage; // Đường dẫn URL của ảnh mặt sau
    private String firstName; // Tên khách hàng
    private String lastName; // Họ khách hàng
    private String nation; // Quốc tịch
    private String address; // Địa chỉ
    private String email; // Email
    private String phone; // Số điện thoại
    private LocalDate dateOfBirth; // Ngày sinh
    private LocalDateTime createdAt = java.time.LocalDateTime.now(); // Thời gian tạo
    private Boolean customer_business;
    private Boolean segment;
    private Boolean deleted = false; // Trạng thái customer
    private LocalDateTime deletedAt; // Thời gian xóa

}
