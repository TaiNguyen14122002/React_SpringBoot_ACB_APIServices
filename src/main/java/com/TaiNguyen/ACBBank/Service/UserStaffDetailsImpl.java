package com.TaiNguyen.ACBBank.Service;

import com.TaiNguyen.ACBBank.Modal.User_Staff_ACBBank;
import com.TaiNguyen.ACBBank.Repository.UserStaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserStaffDetailsImpl implements UserDetailsService  {

    @Autowired
    private UserStaffRepository userStaffRepository;

    @Override
    public UserDetails loadUserByUsername(String Employee_Code) throws UsernameNotFoundException {
        User_Staff_ACBBank staff = userStaffRepository.findByemployeeCode(Employee_Code);

        if (staff == null) {
            throw new UsernameNotFoundException("Không tìm thấy mã nhân viên: " + Employee_Code);
        }
        List<GrantedAuthority> authorities = new ArrayList<>();

        return new org.springframework.security.core.userdetails.User(staff.getEmployeeCode(),
                staff.getPassword(), authorities);
    }




}
