package com.acbtnb.branches_service.utils;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CusResponseMessage {
    // Branch message
    public static final String emptyBranchesMess = "List of branches is empty";
    public static final String existedBranchesMess = "List of branches has been found";
    public static final String insertBranchSuccessMess = "Insert new branch successfully";
    public static final String insertBranchFailedMess = "Insert new branch failed";
    public static final String updateBranchSuccessMess = "Update branch successfully";
    public static final String updateBranchFailedMess = "Update branch failed";
    public static final String notFoundBranchMess = "Branch doesn't exist";
    public static final String existedBranchMess = "Branch was founded";
    public static final String deleteBranchSuccessMess = "Delete branch successfully";
    public static final String deleteBranchFailedMess = "Delete branch failed";

}
