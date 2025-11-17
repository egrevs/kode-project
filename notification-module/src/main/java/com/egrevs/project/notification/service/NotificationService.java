package com.egrevs.project.notification.service;

import com.egrevs.project.domain.entity.notification.Notification;
import com.egrevs.project.domain.entity.user.User;
import com.egrevs.project.domain.repository.NotificationRepository;
import com.egrevs.project.domain.repository.UserRepository;
import com.egrevs.project.shared.dtos.notification.CreateNotificationRequest;
import com.egrevs.project.shared.dtos.notification.NotificationDto;
import com.egrevs.project.shared.dtos.notification.UpdateNotificationRequest;
import com.egrevs.project.shared.exceptions.NotificationNotFoundException;
import com.egrevs.project.shared.exceptions.user.UserNotFoundException;
import com.egrevs.project.shared.mapper.NotificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Transactional
    public NotificationDto createNotification(CreateNotificationRequest request){
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new UserNotFoundException("User with id " + request.userId() + " not found"));

        Notification notification = new Notification();
        notification.setCreatedAt(LocalDateTime.now());
        notification.setUser(user);
        notification.setMessage(request.message());

        Notification savedNotification = notificationRepository.save(notification);

        return NotificationMapper.toDto(savedNotification);
    }

    @Transactional
    public List<NotificationDto> getUserNotifications(String userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));

        return user.getNotifications()
                .stream()
                .map(NotificationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public NotificationDto updateStatus(UpdateNotificationRequest request, String id){
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException("Notification with id " + id + " not found"));

        notification.setStatus(request.status());

        return NotificationMapper.toDto(notificationRepository.save(notification));
    }
}
