package com.egrevs.project.shared.dtos.user;

public record UpdateUserRequest(
        String name,
        String email,
        String password
) {
}
