package org.example.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.entity.Logger;
import org.example.model.service.NotificationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notification")
@Slf4j
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    @PostMapping("/create-log")
    public void createLog(@RequestBody Logger logger){
        log.info(logger.getSender());
        log.info(logger.getMessage());
        log.info(String.valueOf(logger.getLocalDateTime()));
        notificationService.save(logger);
    }
}
