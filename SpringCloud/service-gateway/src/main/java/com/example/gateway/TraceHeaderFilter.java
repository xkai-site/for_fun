package com.example.gateway;

import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class TraceHeaderFilter implements GlobalFilter, Ordered {

    private static final String TRACE_HEADER = "X-Request-Id";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String traceId = exchange.getRequest().getHeaders().getFirst(TRACE_HEADER);
        if (traceId == null || traceId.isBlank()) {
            traceId = UUID.randomUUID().toString();
        }

        ServerHttpRequest updatedRequest = exchange.getRequest().mutate()
                .header(TRACE_HEADER, traceId)
                .build();

        MDC.put("requestId", traceId);
        return chain.filter(exchange.mutate().request(updatedRequest).build())
                .doFinally(signalType -> MDC.remove("requestId"));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
