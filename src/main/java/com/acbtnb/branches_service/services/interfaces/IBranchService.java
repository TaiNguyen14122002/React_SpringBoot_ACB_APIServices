package com.acbtnb.branches_service.services.interfaces;

import com.acbtnb.branches_service.models.dtos.BranchDTO;
import com.acbtnb.branches_service.models.dtos.BulkBranchesDTO;
import com.acbtnb.branches_service.responses.ResponseObject;

import java.util.List;

public interface IBranchService {
    ResponseObject insertBranch(BranchDTO branchDTO);
    ResponseObject insertBulkBranches(BulkBranchesDTO bulkBranchesDTO);
    ResponseObject getBranch(Integer id);
    ResponseObject listBranches();
    ResponseObject deleteBranch(Integer id);
}
