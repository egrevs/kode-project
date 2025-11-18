package com.egrevs.project.shared.dtos.notification;

public record CreateNotificationRequest(
        String userId,
        String message
) {
}
