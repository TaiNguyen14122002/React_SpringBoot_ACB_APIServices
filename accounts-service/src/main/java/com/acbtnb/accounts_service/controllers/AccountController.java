package com.acbtnb.accounts_service.controllers;

import com.acbtnb.accounts_service.client.BranchesClient;
import com.acbtnb.accounts_service.client.CustomersClient;
import com.acbtnb.accounts_service.controllers.interfaces.IAccountController;
import com.acbtnb.accounts_service.models.dtos.AccountDTO;
import com.acbtnb.accounts_service.models.dtos.BulkAccountDTO;
import com.acbtnb.accounts_service.models.entities.Account;
import com.acbtnb.accounts_service.responses.ResponseObject;
import com.acbtnb.accounts_service.services.interfaces.IAccountService;
import com.acbtnb.accounts_service.utils.CusResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Account controller", description = "Account APIs management")
@RestController
@RequestMapping("/accounts")
public class AccountController implements IAccountController {

    @Autowired
    private IAccountService iAccountService;

    @Autowired
    private BranchesClient branchesClient;

    @Autowired
    private CustomersClient customersClient;

    @Override
    @Operation(
            summary = "Insert or update an account ",
            description = "Insert an account with new id or update an account using existing id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = ResponseObject.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @PostMapping("/insert-account")
    public ResponseObject insertAccount(@RequestBody AccountDTO accountDTO) {

        return iAccountService.insertAccount(accountDTO);
    }

    @Override
    @Operation(
            summary = "Insert or update bulk accounts ",
            description = "Insert bulk accounts with new ids or update bulk account using existing ids")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = ResponseObject.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @PostMapping("/insert-bulk-accounts")
    public ResponseObject insertBulkAccount(@RequestBody BulkAccountDTO bulkAccountDTO) {

        for (AccountDTO accDTO : bulkAccountDTO.getAccounts()) {
            // Check existed branch
            ResponseObject branchRes = branchesClient.getBranch(accDTO.getBranch_id());


            if(branchRes.getData() == null)
            {
                return ResponseObject.builder()
                        .status(HttpStatus.NOT_FOUND.name())
                        .message(CusResponseMessage.notFoundBranchMess + " with id = " + accDTO.getBranch_id())
                        .data(null).build();
            }

            // Check existed customer
            ResponseObject customerRes = customersClient.getCustomer(accDTO.getCustomer_id());

            if(customerRes.getData() == null)
            {
                return ResponseObject.builder()
                        .status(HttpStatus.NOT_FOUND.name())
                        .message(CusResponseMessage.notFoundCustomerMess + " with id = " + accDTO.getCustomer_id())
                        .data(null).build();
            }
        }

        return iAccountService.insertBulkAccounts(bulkAccountDTO);
    }

    @Override
    @Operation(
            summary = "List all accounts ",
            description = "List all accounts in database")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = ResponseObject.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping("/list-accounts")
    public ResponseObject listAccounts() {
        return iAccountService.listAccounts();
    }

    @Override
    @Operation(
            summary = "Get an account ",
            description = "Get an account using existing id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = ResponseObject.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping("/get-account")
    public ResponseObject getAccount(@RequestParam("id") String id) {
        return iAccountService.getAccount(id);
    }

    @Override
    @Operation(
            summary = "Delete an account ",
            description = "Delete an account using existing id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = ResponseObject.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @PatchMapping("/delete-account")
    public ResponseObject deleteAccount(@RequestParam("id") String id) {
        return iAccountService.deleteAccount(id);
    }
}
