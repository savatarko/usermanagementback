package org.raf.usermanagement.db;

import org.raf.usermanagement.domain.ScheduleError;
import org.raf.usermanagement.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScheduleErrorRepository extends JpaRepository<ScheduleError, Long> {

    Optional<List<ScheduleError>> findAllByUser(User user);
}
