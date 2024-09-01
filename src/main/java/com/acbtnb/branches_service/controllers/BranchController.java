package com.acbtnb.branches_service.controllers;

import com.acbtnb.branches_service.controllers.interfaces.IBranchesController;
import com.acbtnb.branches_service.models.dtos.BranchDTO;
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
    @PostMapping()
    public ResponseObject insertBranch(@RequestBody() BranchDTO branchDTO ) {
        return iBranchService.insertBranch(branchDTO);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseObject updateBranch(@PathVariable Integer id, @RequestBody() BranchDTO branchDTO ) {
        return iBranchService.updateBranch(id, branchDTO);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseObject deleteBranch(@PathVariable Integer id) {
        return iBranchService.deleteBranch(id);
    }

    @Override
    @GetMapping("/list-branches")
    public ResponseObject listBranches() {
        return null;
    }

    @Override
    @GetMapping("/get-branch")
    public ResponseObject getBranch(@RequestParam("id") Integer id) {

        return iBranchService.getBranch(id);
    }
}
