package com.acbtnb.branches_service.controllers;

import com.acbtnb.branches_service.controllers.interfaces.IBranchesController;
import com.acbtnb.branches_service.models.dtos.BranchDTO;
import com.acbtnb.branches_service.models.dtos.BulkBranchesDTO;
import com.acbtnb.branches_service.responses.ResponseObject;
import com.acbtnb.branches_service.services.interfaces.IBranchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Branch controller", description = "Branch APIs management")
@RestController
@RequestMapping("/branches")
public class BranchController implements IBranchesController {

    @Autowired
    private IBranchService iBranchService;

    @Override
    @Operation(
            summary = "Insert or update a branch ",
            description = "Insert a branch with new id or update a branch using existing id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = ResponseObject.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @PostMapping("/insert-branch")
    public ResponseObject insertBulkBranch(@RequestBody BranchDTO branchDTO) {
        return iBranchService.insertBranch(branchDTO);
    }

    @Override
    @Operation(
            summary = "Insert or update bulk branches ",
            description = "Insert bulk branches with new ids or update bulk branches using existing ids")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = ResponseObject.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @PostMapping("/insert-bulk-branches")
    public ResponseObject insertBulkBranch(@RequestBody BulkBranchesDTO bulkBranchesDTO) {
        return iBranchService.insertBulkBranches(bulkBranchesDTO);
    }

    @Override
    @Operation(
            summary = "List all branches ",
            description = "List all branches in database")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = ResponseObject.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping("/list-branches")
    public ResponseObject listBranches() {
        return iBranchService.listBranches();
    }

    @Override
    @Operation(
            summary = "Get a branch  ",
            description = "Get a branch using existing id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = ResponseObject.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping("/get-branch")
    public ResponseObject getBranch(@RequestParam("id") Integer id) {
        return iBranchService.getBranch(id);
    }

    @Override
    @Operation(
            summary = "Delete a branch ",
            description = "Delete a branch using existing id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = ResponseObject.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @PatchMapping("/delete-branch")
    public ResponseObject deleteBranch(@RequestParam("id") Integer id) {
        return iBranchService.deleteBranch(id);
    }
}
