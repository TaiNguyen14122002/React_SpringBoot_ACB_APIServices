package com.acbtnb.branches_service.controllers.interfaces;
import com.acbtnb.branches_service.models.dtos.BranchDTO;
import com.acbtnb.branches_service.models.dtos.BulkBranchesDTO;
import com.acbtnb.branches_service.responses.ResponseObject;

import java.util.List;

public interface IBranchesController {
    ResponseObject insertBranch(BranchDTO branchDTO);
    ResponseObject insertBranch(BulkBranchesDTO bulkBranchesDTO);
    ResponseObject listBranches();
    ResponseObject getBranch(Integer id);
    ResponseObject deleteBranch(Integer id);
}