package org.raf.usermanagement.db;

import org.raf.usermanagement.domain.User;
import org.raf.usermanagement.domain.Usisivac;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsisivacRepository extends JpaRepository<Usisivac, Long> {

    List<Usisivac> findAllByAddedBy(User user);
}
