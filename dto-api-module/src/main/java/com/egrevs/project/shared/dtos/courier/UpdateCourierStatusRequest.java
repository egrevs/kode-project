package com.egrevs.project.shared.dtos.courier;

import com.egrevs.project.domain.enums.CourierStatus;

public record UpdateCourierStatusRequest(
        CourierStatus status
) {
}
