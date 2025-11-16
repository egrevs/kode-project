package com.egrevs.project.shared.mapper;

import com.egrevs.project.domain.entity.courier.Courier;
import com.egrevs.project.shared.dtos.courier.CourierDto;
import org.springframework.stereotype.Component;

@Component
public class CourierMapper {

    public static CourierDto toDto(Courier courier) {
        return new CourierDto(
                courier.getId(),
                courier.getName(),
                courier.getEmail(),
                courier.getLogin(),
                courier.getPassword(),
                courier.getRole(),
                courier.getCourierStatus(),
                courier.getCreated_at(),
                courier.getOrder().stream().map(OrderMapper::toDto).toList()
        );
    }
}
