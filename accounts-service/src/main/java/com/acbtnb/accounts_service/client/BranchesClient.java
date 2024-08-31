package com.acbtnb.accounts_service.client;

import com.acbtnb.accounts_service.responses.ResponseObject;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface BranchesClient {
    @GetExchange("/branches/get-branch")
    public ResponseObject getBranch(@RequestParam("id") Integer id);
}
