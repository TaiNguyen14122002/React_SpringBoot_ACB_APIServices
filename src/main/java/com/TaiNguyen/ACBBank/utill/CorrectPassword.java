package com.TaiNguyen.ACBBank.utill;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CorrectPassword {
    private static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Phương thức để mã hóa mật khẩu
    public static String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    // Phương thức để kiểm tra mật khẩu có khớp không
    public static boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

}
