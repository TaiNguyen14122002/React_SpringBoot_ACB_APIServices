package com.acbtnb.branches_service.controllers.interfaces;
import com.acbtnb.branches_service.responses.ResponseObject;

public interface IBranchesController {
    ResponseObject insertBranch();
    ResponseObject listBranches();
    ResponseObject getBranch(Integer id);
    ResponseObject deleteBranch();
}