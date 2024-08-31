package com.acbtnb.accounts_service.utils;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CusResponseMessage {
    // Account messages
    public static final String emptyAccountsMess = "List of accounts is empty";
    public static final String existedAccountsMess = "List of accounts has been found";
    public static final String insertAccountSuccessMess = "Insert new account successfully";
    public static final String insertBulkAccountSuccessMess = "Insert list accounts successfully";
    public static final String updateAccountSuccessMess = "Update account successfully";
    public static final String notFoundAccountMess = "Account doesn't exist";
    public static final String existedAccountMess = "Account was founded";
    public static final String deletedAccountSuccessMess = "Delete account successfully";

    // Account type messages
    public static final String emptyAccountTypesMess = "List of account types is empty";
    public static final String existedAccountTypesMess = "List of account types has been found";
    public static final String insertAccountTypeSuccessMess = "Insert new account type successfully";
    public static final String insertBulkAccountTypeSuccessMess = "Insert list account types successfully";
    public static final String updateAccountTypeSuccessMess = "Update account type successfully";
    public static final String notFoundAccountTypeMess = "Account type doesn't exist";
    public static final String existedAccountTypeMess = "Account type was founded";
    public static final String deletedAccountTypeSuccessMess = "Delete account type successfully";

    // Branch messages
    public static final String notFoundBranchMess = "Branch doesn't exist";
    public static final String notFoundCustomerMess = "Customer doesn't exist";

}
