package com.egrevs.project.shared.dtos.notification;

import com.egrevs.project.domain.enums.NotificationStatus;

public record UpdateNotificationRequest(
        NotificationStatus status
) {
}
