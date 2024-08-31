package com.acbtnb.branches_service.services.interfaces;

import com.acbtnb.branches_service.models.dtos.BranchDTO;
import com.acbtnb.branches_service.responses.ResponseObject;

public interface IBranchService {
    ResponseObject insertBranch(BranchDTO branchDTO);
    ResponseObject getBranch(Integer id);
    ResponseObject listBranches();
    ResponseObject deleteBranch(Integer id);
}
