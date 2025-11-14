package com.egrevs.project.gateway.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(
        @NotBlank String name,
        @NotBlank String email,
        @NotBlank String login,
        @NotBlank String password
) {
}
