package com.egrevs.project.domain.repository;

import com.egrevs.project.domain.entity.courier.Courier;
import com.egrevs.project.domain.enums.CourierStatus;
import com.egrevs.project.domain.util.DataUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CourierRepositoryTest {

    @Autowired
    private CourierRepository courierRepository;

    @Test
    @DisplayName("Test courier findByLogin functionality")
    void givenCourierWithLogin_whenFindByLogin_thenReturnCourier() {
        // given
        Courier courier = DataUtils.createCourier(CourierStatus.AVAILABLE);
        courier.setLogin("courier_login");
        courierRepository.save(courier);

        // when
        Optional<Courier> found = courierRepository.findByLogin("courier_login");

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getLogin()).isEqualTo("courier_login");
    }

    @Test
    @DisplayName("Test find couriers by status functionality")
    void givenCouriersWithDifferentStatuses_whenFindByCourierStatus_thenReturnOnlyMatching() {
        // given
        Courier available = DataUtils.createCourier(CourierStatus.AVAILABLE);
        Courier busy = DataUtils.createCourier(CourierStatus.BUSY);
        courierRepository.saveAll(List.of(available, busy));

        // when
        List<Courier> result = courierRepository.findByCourierStatus(CourierStatus.AVAILABLE);

        // then
        assertThat(result)
                .hasSize(1)
                .first()
                .extracting(Courier::getCourierStatus)
                .isEqualTo(CourierStatus.AVAILABLE);
    }
}

