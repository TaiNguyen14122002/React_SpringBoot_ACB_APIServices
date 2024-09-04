package com.TaiNguyen.ACBBank.utill;

import java.security.SecureRandom;

public class PasswordUtil {

    private static final String UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER_CASE = UPPER_CASE.toLowerCase();
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_=+[]{}|;:,.<>?";

    private static final int PASSWORD_LENGTH = 12;

    public static String generateRandomPassword(){
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
        SecureRandom random = new SecureRandom();

        password.append(UPPER_CASE.charAt(random.nextInt(UPPER_CASE.length())));
        password.append(LOWER_CASE.charAt(random.nextInt(LOWER_CASE.length())));
        password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        password.append(SPECIAL_CHARACTERS.charAt(random.nextInt(SPECIAL_CHARACTERS.length())));

        String allCharacters = UPPER_CASE + LOWER_CASE + DIGITS + SPECIAL_CHARACTERS;
        for( int i =8; i < PASSWORD_LENGTH; i++){
            password.append(allCharacters.charAt(random.nextInt(allCharacters.length())));
        }

        return password.toString();
    }
}
