package com.egrevs.project.notification.controller;

import com.egrevs.project.notification.service.NotificationService;
import com.egrevs.project.shared.dtos.notification.CreateNotificationRequest;
import com.egrevs.project.shared.dtos.notification.NotificationDto;
import com.egrevs.project.shared.dtos.notification.UpdateNotificationRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "Создать уведомление")
    @PostMapping
    public ResponseEntity<NotificationDto> createNotification(@RequestBody CreateNotificationRequest request){
        return ResponseEntity.ok(notificationService.createNotification(request));
    }

    @Operation(summary = "Получить все уведомления пользователя")
    @GetMapping
    public ResponseEntity<List<NotificationDto>> getUserNotifications(@RequestParam String id){
        return ResponseEntity.ok(notificationService.getUserNotifications(id));
    }

    @Operation(summary = "Обновить статус уведомления")
    @PatchMapping("/{id}/status")
    public ResponseEntity<NotificationDto> updateStatus(@PathVariable String id,
                                                        @RequestBody UpdateNotificationRequest request){
        return ResponseEntity.ok(notificationService.updateStatus(request, id));
    }
}
