package com.egrevs.project.gateway.dto;

public record UpdateUserRequest(
        String name,
        String email,
        String password
) {
}
