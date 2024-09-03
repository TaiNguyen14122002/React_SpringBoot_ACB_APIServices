package com.TaiNguyen.ACBBank.utill;

import com.TaiNguyen.ACBBank.Modal.User_Staff_ACBBank;
import com.TaiNguyen.ACBBank.Repository.UserStaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

    @Autowired
    private UserStaffRepository userStaffRepository;

    @Autowired
    private EmailUtil emailUtil;

    private ConcurrentHashMap<String, String> otpStore = new ConcurrentHashMap<>();

    public void generateAndSendOtp(String email) throws Exception {
        User_Staff_ACBBank user = userStaffRepository.findByEmail(email);
        if(user == null) {
            throw new Exception("User not found");
        }

        String otp = String.format("%06d", new Random().nextInt(999999));

        otpStore.put(email, otp);

        String subject = "Your OTP Code";
        String message = "Your OTP Code is " + otp;

        emailUtil.sendEmail(email, subject, message);
    }

    public boolean verifyOtp(String email, String otp) {
        return otp.equals(otpStore.get(email));
    }
}
