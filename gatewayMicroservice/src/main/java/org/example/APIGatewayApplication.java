package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;

import java.time.Duration;
import java.time.LocalTime;

@SpringBootApplication
public class APIGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(APIGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder
                .routes()
                .route(r ->
                        r.path("/api/**")
                                .filters(gatewayFilterSpec -> gatewayFilterSpec
                                        .rewritePath("/api/v1/product/(?<remaining>.*)","/api/v1/product/${remaining}")
                                        .addRequestHeader("my_response_header", String.valueOf(LocalTime.now()))
                                        .circuitBreaker(config -> config
                                                .setName("apiGateWayCircuitBreaker")
                                                .setFallbackUri("forward:/circuitBreaker/fallback")
                                        )
                                )

                                .uri("lb://PRODUCT")
                ).route(predicateSpec -> predicateSpec
                        .path("/api/v1/coupon/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec
                                .rewritePath("/api/v1/coupons/(?<remaining>.*)","/api/v1/coupons/${remaining}")
                                .retry(retryConfig -> retryConfig
                                        .setRetries(3)
                                        .setMethods(HttpMethod.GET)
                                        .setBackoff(Duration.ofMillis(100), Duration.ofMillis(1000), 2 , true)
                                )
                        )
                        .uri("lb://DISCOUNT")
                ).build();
    }
}
