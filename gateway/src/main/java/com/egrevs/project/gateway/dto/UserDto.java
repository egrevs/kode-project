package com.egrevs.project.gateway.dto;

import com.egrevs.project.gateway.entity.UserRole;

import java.time.LocalDateTime;

public record UserDto(
        String id,
        String name,
        String email,
        String login,
        UserRole role,
        LocalDateTime created_at,
        LocalDateTime updated_at
) {
}
