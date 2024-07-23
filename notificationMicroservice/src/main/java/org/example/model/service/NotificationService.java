package org.example.model.service;

import lombok.RequiredArgsConstructor;
import org.example.model.entity.Logger;
import org.example.model.repository.NotificationRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void save(Logger logger) {
        notificationRepository.save(logger);
    }
}
