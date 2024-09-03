package com.TaiNguyen.ACBBank.Service;

import com.TaiNguyen.ACBBank.Modal.User_Staff_ACBBank;

public interface UserService {
    User_Staff_ACBBank findUserByEmail(String email) throws Exception;
    String forgotPassword(String email) throws Exception;
    boolean resetPassword(String email, String newPassword, String otp) throws Exception;
    boolean changePassword(String oldPassword, String newPassword, String token ) throws Exception;
}
