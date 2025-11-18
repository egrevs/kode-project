package com.egrevs.project.shared.dtos.user;

import com.egrevs.project.domain.enums.UserRole;

import java.time.LocalDateTime;

public record UserHistoryDto(
        String userId,
        String name,
        String email,
        String login,
        UserRole role,
        LocalDateTime validFrom,
        LocalDateTime validTo
) {}