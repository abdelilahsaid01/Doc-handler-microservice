package com.example.gatewayservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class RequestFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        log.info("Request -> Method: {}, URI: {}", request.getMethodValue(), request.getURI());
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

/*    private void logRequestAndResponse(ServerHttpRequest request, ServerHttpResponse response) {
        log.info("Request -> Method: {}, URI: {}", request.getMethodValue(), request.getURI());

        response.beforeCommit(() ->
                Mono.defer(() -> {
                    // Log response information
                    log.info("Response -> Status code: {}", response.getStatusCode());
                    return Mono.empty();
                })
        );
    }*/
}
