package org.example.notification;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("NOTIFICATION")
public interface LoggerClient {
    @PostMapping("/api/v1/notification/create-log")
    LoggerDTO createLog(LoggerDTO logger);
}
