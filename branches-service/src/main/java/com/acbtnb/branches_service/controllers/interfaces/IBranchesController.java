package com.acbtnb.branches_service.controllers.interfaces;
import com.acbtnb.branches_service.models.dtos.BranchDTO;
import com.acbtnb.branches_service.models.dtos.BulkBranchesDTO;
import com.acbtnb.branches_service.responses.ResponseObject;

public interface IBranchesController {
    ResponseObject insertBulkBranch(BranchDTO branchDTO);
    ResponseObject insertBulkBranch(BulkBranchesDTO bulkBranchesDTO);
    ResponseObject listBranches();
    ResponseObject getBranch(Integer id);
    ResponseObject deleteBranch(Integer id);
}