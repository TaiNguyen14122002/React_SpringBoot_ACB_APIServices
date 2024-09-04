package com.acbtnb.branches_service.controllers;

import com.acbtnb.branches_service.controllers.interfaces.IBranchesController;
import com.acbtnb.branches_service.models.dtos.BranchDTO;
import com.acbtnb.branches_service.models.dtos.BulkBranchesDTO;
import com.acbtnb.branches_service.responses.ResponseObject;
import com.acbtnb.branches_service.services.interfaces.IBranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/branches")
public class BranchController implements IBranchesController {

    @Autowired
    private IBranchService iBranchService;

    @Override
    @PostMapping("/insert-branch")
    public ResponseObject insertBulkBranch(@RequestBody BranchDTO branchDTO) {
        return iBranchService.insertBranch(branchDTO);
    }

    @Override
    @PostMapping("/insert-bulk-branches")
    public ResponseObject insertBulkBranch(@RequestBody BulkBranchesDTO bulkBranchesDTO) {
        return iBranchService.insertBulkBranches(bulkBranchesDTO);
    }

    @Override
    @GetMapping("/list-branches")
    public ResponseObject listBranches() {
        return iBranchService.listBranches();
    }

    @Override
    @GetMapping("/get-branch")
    public ResponseObject getBranch(@RequestParam("id") Integer id) {
        return iBranchService.getBranch(id);
    }

    @Override
    @PatchMapping("/delete-branch")
    public ResponseObject deleteBranch(@RequestParam("id") Integer id) {
        return iBranchService.deleteBranch(id);
    }
}
