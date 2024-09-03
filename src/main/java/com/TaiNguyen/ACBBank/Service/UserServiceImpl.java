package com.TaiNguyen.ACBBank.Service;

import com.TaiNguyen.ACBBank.Modal.User_Staff_ACBBank;
import com.TaiNguyen.ACBBank.Repository.UserStaffRepository;
import com.TaiNguyen.ACBBank.utill.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserStaffRepository userStaffRepository;

    @Autowired
    private OtpService otpService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public User_Staff_ACBBank findUserByEmail(String email) throws Exception {
        User_Staff_ACBBank user = userStaffRepository.findByEmail(email);
        if(user == null){
            throw new Exception("user not found");
        }
        return user;
    }

    @Override
    public String forgotPassword(String email) throws Exception {
        otpService.generateAndSendOtp(email);
        return "OTP has been sent to your email.";
    }

    @Override
    public boolean resetPassword(String email, String newPassword, String otp) throws Exception {
        if(!otpService.verifyOtp(email, otp)){
            throw new Exception("Invalid OTP");
        }
        User_Staff_ACBBank user = findUserByEmail(email);
        user.setPassword(passwordEncoder.encode(newPassword));
        userStaffRepository.save(user);
        return true;
    }


}
