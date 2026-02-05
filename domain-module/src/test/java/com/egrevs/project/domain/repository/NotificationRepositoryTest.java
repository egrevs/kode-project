package com.egrevs.project.domain.repository;

import com.egrevs.project.domain.entity.notification.Notification;
import com.egrevs.project.domain.entity.user.User;
import com.egrevs.project.domain.enums.NotificationStatus;
import com.egrevs.project.domain.enums.UserRole;
import com.egrevs.project.domain.util.DataUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class NotificationRepositoryTest {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Test notification save and findById functionality")
    void givenNotificationForUser_whenSaveAndFindById_thenNotificationReturned() {
        // given
        User user = DataUtils.createUser(UserRole.USER);
        userRepository.save(user);

        Notification notification = DataUtils.createNotification(user, NotificationStatus.UNREAD);
        Notification saved = notificationRepository.save(notification);

        // when
        Notification found = notificationRepository.findById(saved.getId()).orElseThrow();

        // then
        assertThat(found.getUser().getId()).isEqualTo(user.getId());
        assertThat(found.getStatus()).isEqualTo(NotificationStatus.UNREAD);
    }
}

