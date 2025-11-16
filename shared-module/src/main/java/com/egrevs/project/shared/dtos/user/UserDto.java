package com.egrevs.project.shared.dtos.user;

import com.egrevs.project.domain.enums.UserRole;

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
