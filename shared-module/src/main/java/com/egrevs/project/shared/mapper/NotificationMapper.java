package com.egrevs.project.shared.mapper;

import com.egrevs.project.domain.entity.notification.Notification;
import com.egrevs.project.shared.dtos.notification.NotificationDto;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public static NotificationDto toDto(Notification notification) {
        return new NotificationDto(
                notification.getId(),
                notification.getUser().getId(),
                notification.getMessage(),
                notification.getStatus(),
                notification.getCreatedAt()
        );
    }
}
