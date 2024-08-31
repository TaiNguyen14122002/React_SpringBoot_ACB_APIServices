package com.acbtnb.accounts_service.utils;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CusResponseMessage {
    // Branch message
    public static final String emptyAccountsMess = "List of accounts is empty";
    public static final String existedAccountsMess = "List of accounts has been found";
    public static final String insertAccountSuccessMess = "Insert new account successfully";
    public static final String updateAccountSuccessMess = "Update account successfully";
    public static final String notFoundAccountMess = "Account doesn't exist";
    public static final String existedAccountMess = "Account was founded";
    public static final String deletedAccountSuccessMess = "Delete account successfully";

}
