package org.raf.usermanagement.db;

import org.raf.usermanagement.domain.Permission;
import org.raf.usermanagement.domain.User;
import org.raf.usermanagement.domain.UserPermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserPermissionRepository extends JpaRepository<UserPermission, Long> {

    Optional<UserPermission> findUserPermissionByUserAndPermission(User user, Permission permission);

    Optional<List<UserPermission>> findUserPermissionsByUser(User user);
}
