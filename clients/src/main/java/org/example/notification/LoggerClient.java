package org.example.notification;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "NOTIFICATION", fallback = LoggerClientFallBack.class)
public interface LoggerClient {
    @PostMapping("/api/v1/notification/create-log")
    LoggerDTO createLog(LoggerDTO logger);
}
