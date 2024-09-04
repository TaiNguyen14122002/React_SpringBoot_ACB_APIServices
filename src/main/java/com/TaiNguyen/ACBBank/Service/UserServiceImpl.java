package com.TaiNguyen.ACBBank.Service;

import com.TaiNguyen.ACBBank.Config.JwtProvider;
import com.TaiNguyen.ACBBank.Modal.User_Staff_ACBBank;
import com.TaiNguyen.ACBBank.Repository.UserStaffRepository;
import com.TaiNguyen.ACBBank.utill.EmailUtil;
import com.TaiNguyen.ACBBank.utill.OtpService;
import com.TaiNguyen.ACBBank.utill.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserStaffRepository userStaffRepository;

    @Autowired
    private OtpService otpService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private EmailUtil emailUtil;


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
        User_Staff_ACBBank user = findUserByEmail(email);
        if ( user == null){
            throw new Exception("user not found");
        }
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

    @Override
    public boolean changePassword(String oldPassword, String newPassword, String token) throws Exception {
        String email = jwtProvider.getEmailFromToken(token);
        User_Staff_ACBBank user = findUserByEmail(email);
        if(user == null){
            throw new Exception("user not found");
        }

        if(!passwordEncoder.matches(oldPassword, user.getPassword())){
            throw new Exception("Old password does not incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userStaffRepository.save(user);
        return true;
    }

//    @Override
//    public void addEmployee(User_Staff_ACBBank newEmployee, String token) throws Exception {
//        String email = jwtProvider.getEmailFromToken(token);
//        User_Staff_ACBBank currentUser = findUserByEmail(email);
//        if(!currentUser.getRoles().contains("ROLE_ADMIN")){
//            throw new Exception("You do not have permission to add a new employee");
//        }
//
////        newEmployee.setPassword(passwordEncoder.encode(newEmployee.getPassword()));
//
//        String rawPassword = PasswordUtil.generateRandomPassword();
//        String encodedPassword = passwordEncoder.encode(rawPassword);
//        newEmployee.setPassword(encodedPassword);
//
//        Set<String> roles = newEmployee.getRoles();
//        roles.add("ROLE_USER");
//        newEmployee.setRoles(roles);
//        userStaffRepository.save(newEmployee);
//
//        String subject = "Your new account information";
//        String text = String.format("Hello %s,\n\nYour account has been created.\nUsername: %s\nPassword: %s\n\nBest Regards,\nYour Company",
//                newEmployee.getFullName(), newEmployee.getEmail(), newEmployee.getEmployeeCode(), encodedPassword);
//
//        emailUtil.sendEmail(newEmployee.getEmail(), subject, text);
//
//    }


}
