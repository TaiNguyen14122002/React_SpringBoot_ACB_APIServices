package com.acbtnb.branches_service.controllers;

import com.acbtnb.branches_service.controllers.interfaces.IBranchesController;
import com.acbtnb.branches_service.responses.ResponseObject;
import com.acbtnb.branches_service.services.interfaces.IBranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/branches")
public class BranchController implements IBranchesController {

    @Autowired
    private IBranchService iBranchService;

    @Override
    public ResponseObject insertBranch() {
        return null;
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

    @Override
    public ResponseObject deleteBranch() {
        return null;
    }
}
