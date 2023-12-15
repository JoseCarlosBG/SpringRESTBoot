package com.epam.esm.repo;

import com.epam.esm.model.entity.ERole;
import com.epam.esm.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ERole roleName);
}
