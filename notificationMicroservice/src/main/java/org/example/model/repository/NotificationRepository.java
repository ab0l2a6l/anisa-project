package org.example.model.repository;

import org.example.model.entity.Logger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Logger, Long> {
}
