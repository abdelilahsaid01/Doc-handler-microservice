package com.example.gatewayservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
public class ResponseFilter {
    @Bean
    @Order(-2)
    public GlobalFilter resFilter(){
        return (var exchange, var chain) -> chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {
                    var response = exchange.getResponse();
                    log.info("Response -> Status code: {}", response.getStatusCode());
                    response.setRawStatusCode(201);
                    exchange.mutate().response(response).build();
                }));
    }
}
