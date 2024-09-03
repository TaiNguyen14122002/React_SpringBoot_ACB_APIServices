package com.TaiNguyen.ACBBank.Repository;

import com.TaiNguyen.ACBBank.Modal.User_Staff_ACBBank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStaffRepository extends JpaRepository<User_Staff_ACBBank, Long> {
    User_Staff_ACBBank findByEmail(String email);
    User_Staff_ACBBank findByemployeeCode( String employeeCode);
}
