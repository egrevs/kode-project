package com.egrevs.project.shared.dtos.courier;

public record CreateCourierRequest(
        String name,
        String login,
        String email,
        String password
) {
}
