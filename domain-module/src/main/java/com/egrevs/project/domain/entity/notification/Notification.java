package com.egrevs.project.domain.entity.notification;

import com.egrevs.project.domain.entity.user.User;
import com.egrevs.project.domain.enums.NotificationStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "message")
    private String message;

    @Column(name = "status")
    private NotificationStatus status = NotificationStatus.UNREAD;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

