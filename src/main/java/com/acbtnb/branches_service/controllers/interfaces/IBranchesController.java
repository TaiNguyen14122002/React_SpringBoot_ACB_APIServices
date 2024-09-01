package com.acbtnb.branches_service.controllers.interfaces;
import com.acbtnb.branches_service.models.dtos.BranchDTO;
import com.acbtnb.branches_service.responses.ResponseObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface IBranchesController {
    ResponseObject insertBranch(BranchDTO branchDTO);
    ResponseObject updateBranch(Integer id, BranchDTO branchDTO);
    ResponseObject listBranches();
    ResponseObject getBranch(Integer id);
    ResponseObject deleteBranch(Integer id);
}