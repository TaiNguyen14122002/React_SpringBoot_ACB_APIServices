package com.acbtnb.accounts_service.utils;

public class RandomString {
    // Function to generate a random string of length n
    public static String getNumericString(int n)
    {

        // choose a Character random from this String
        String NumericString = "0123456789";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(NumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(NumericString
                    .charAt(index));
        }

        return sb.toString();
    }
}
