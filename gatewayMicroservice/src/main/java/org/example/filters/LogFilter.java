package org.example.filters;


import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalTime;

@Component
@Order(1)
@Slf4j
public class LogFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // pre
        long start = System.currentTimeMillis();
        ApplicationContext applicationContext = exchange.getApplicationContext();
        assert applicationContext != null;
        log.info("getApplicationName: " + applicationContext.getApplicationName());
        log.info("getApplicationId: " + applicationContext.getId());
        log.info("getApplicationDisplayName: " + applicationContext.getDisplayName());
        return chain.filter(exchange).then(
                Mono.fromRunnable(
                        () -> {
                            //post
                            long end = System.currentTimeMillis();
                            log.info("request time: " + (end - start) + "ms" );
                         }
                )
        );
    }
}
