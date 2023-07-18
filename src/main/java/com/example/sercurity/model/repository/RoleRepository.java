package com.example.sercurity.model.repository;

import com.example.sercurity.model.entity.ERole;
import com.example.sercurity.model.entity.Roles;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface RoleRepository extends Repository<Roles,Long> {
    Optional<Roles> findByRoleName(ERole roleName);
}
