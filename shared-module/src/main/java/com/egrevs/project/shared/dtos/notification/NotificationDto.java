package com.egrevs.project.shared.dtos.notification;

import com.egrevs.project.domain.enums.NotificationStatus;

import java.time.LocalDateTime;

public record NotificationDto(
        String id,
        String userId,
        String message,
        NotificationStatus status,
        LocalDateTime createdAt
) {
}
