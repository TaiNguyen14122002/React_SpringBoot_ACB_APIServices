package com.acbtnb.accounts_service.config;

import com.acbtnb.accounts_service.client.BranchesClient;
import com.acbtnb.accounts_service.client.CustomersClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancedExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class WebClientConfig {
    @Autowired
    private LoadBalancedExchangeFilterFunction filterFunction;


    @Bean
    public WebClient branchesWebClient() {
        return WebClient.builder()
                .baseUrl("http://branches-service/api/v1")
                .filter(filterFunction)
                .build();
    }

    @Bean
    public BranchesClient branchesClient() {
        HttpServiceProxyFactory httpServiceProxyFactory
                = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(branchesWebClient()))
                .build();
        return httpServiceProxyFactory.createClient(BranchesClient.class);
    }

    @Bean
    public WebClient customersWebClient() {
        return WebClient.builder()
                .baseUrl("http://customers-service/api/v1")
                .filter(filterFunction)
                .build();
    }

    @Bean
    public CustomersClient customersClient() {
        HttpServiceProxyFactory httpServiceProxyFactory
                = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(customersWebClient()))
                .build();
        return httpServiceProxyFactory.createClient(CustomersClient.class);
    }
}
