package com.egrevs.project.domain.repository;

import com.egrevs.project.domain.entity.user.UserHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserHistoryRepository extends JpaRepository<UserHistory, String> {

    @Modifying
    @Query("""
            update UserHistory h set h.validTo = :to 
            where h.userId = :userId and h.validTo is null """)
    void closeActiveUserVersion(String userId, LocalDateTime to);

    @Query("""
            select h from UserHistory h 
            where h.userId =: userId 
            and h.validFrom <=:now
            and h.validTo is null """)
    Optional <UserHistory> findVersionAtTime(String userId, LocalDateTime now);
}
