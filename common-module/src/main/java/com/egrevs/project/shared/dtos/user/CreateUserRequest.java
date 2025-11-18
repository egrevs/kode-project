package com.egrevs.project.shared.dtos.user;

import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(
        @NotBlank String name,
        @NotBlank String email,
        @NotBlank String login,
        @NotBlank String password
) {
}
