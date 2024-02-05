package org.raf.usermanagement.db;

import org.raf.usermanagement.domain.ScheduledAction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduledActionRepository extends JpaRepository<ScheduledAction, Long> {
    List<ScheduledAction> findAllByTime(LocalDateTime time);
}
