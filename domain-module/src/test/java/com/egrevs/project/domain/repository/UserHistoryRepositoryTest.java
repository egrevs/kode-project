package com.egrevs.project.domain.repository;

import com.egrevs.project.domain.entity.user.UserHistory;
import com.egrevs.project.domain.enums.UserRole;
import com.egrevs.project.domain.util.DataUtils;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class UserHistoryRepositoryTest {

    @Autowired
    private UserHistoryRepository userHistoryRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @Transactional
    @Rollback
    @DisplayName("Test closing active user history version functionality")
    void givenActiveAndClosedHistoryForUser_whenCloseActiveUserVersion_thenOnlyActiveGetsClosed() {
        String userId = "user-1";

        LocalDateTime oldValidTo = LocalDateTime.now().minusDays(1);

        // closed history version
        UserHistory closedVersion = DataUtils.createUserHistory(
                userId,
                UserRole.USER,
                LocalDateTime.now().minusDays(2),
                oldValidTo
        );

        // active history version (validTo == null)
        UserHistory activeVersion = DataUtils.createUserHistory(
                userId,
                UserRole.USER,
                LocalDateTime.now().minusHours(5),
                null
        );

        // history for another user, should not be affected
        UserHistory otherUserHistory = DataUtils.createUserHistory(
                "user-2",
                UserRole.COURIER,
                LocalDateTime.now().minusDays(1),
                null
        );

        userHistoryRepository.saveAll(List.of(closedVersion, activeVersion, otherUserHistory));

        LocalDateTime closeTime = LocalDateTime.now();
        userHistoryRepository.closeActiveUserVersion(userId, closeTime);

        // ensure persistence context is in sync with DB after bulk update
        entityManager.flush();
        entityManager.clear();

        List<UserHistory> allHistories = userHistoryRepository.findAll();
        assertThat(allHistories).hasSize(3);

        UserHistory reloadedClosedVersion = allHistories.stream()
                .filter(h -> h.getId().equals(closedVersion.getId()))
                .findFirst()
                .orElseThrow();

        UserHistory reloadedActiveVersion = allHistories.stream()
                .filter(h -> h.getId().equals(activeVersion.getId()))
                .findFirst()
                .orElseThrow();

        UserHistory reloadedOtherUserHistory = allHistories.stream()
                .filter(h -> h.getId().equals(otherUserHistory.getId()))
                .findFirst()
                .orElseThrow();

        // previously closed version must keep its old validTo
        assertThat(reloadedClosedVersion.getValidTo()).isEqualTo(oldValidTo);

        // active version must receive new validTo
        assertThat(reloadedActiveVersion.getValidTo()).isEqualTo(closeTime);

        // history of another user must not be affected
        assertThat(reloadedOtherUserHistory.getValidTo()).isNull();
    }

    @Test
    @DisplayName("Test finding active user history version by time functionality")
    void givenActiveAndClosedVersions_whenFindVersionAtTime_thenReturnActiveVersion() {
        String userId = "user-1";

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime activeFrom = now.minusHours(1);

        // active version for user
        UserHistory activeVersion = DataUtils.createUserHistory(
                userId,
                UserRole.USER,
                activeFrom,
                null
        );

        // closed version for same user (should not be returned)
        UserHistory closedVersion = DataUtils.createUserHistory(
                userId,
                UserRole.USER,
                now.minusDays(2),
                now.minusDays(1)
        );

        userHistoryRepository.saveAll(List.of(activeVersion, closedVersion));

        Optional<UserHistory> result = userHistoryRepository.findVersionAtTime(userId, now);

        assertTrue(result.isPresent());
        assertThat(result.get().getId()).isEqualTo(activeVersion.getId());
        assertThat(result.get().getValidTo()).isNull();
        assertThat(result.get().getValidFrom()).isEqualTo(activeFrom);
    }

    @Test
    @DisplayName("Test returning empty when no active user history at given time functionality")
    void givenFutureVersionOnly_whenFindVersionAtTimeNow_thenReturnEmpty() {
        String userId = "user-1";

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime activeFrom = now.plusHours(1); // starts in the future

        UserHistory futureVersion = DataUtils.createUserHistory(
                userId,
                UserRole.USER,
                activeFrom,
                null
        );

        userHistoryRepository.save(futureVersion);

        Optional<UserHistory> result = userHistoryRepository.findVersionAtTime(userId, now);

        assertFalse(result.isPresent());
    }
}

