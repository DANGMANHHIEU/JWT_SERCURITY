package com.example.sercurity.model.service;

import com.example.sercurity.model.entity.ERole;
import com.example.sercurity.model.entity.Roles;

import java.util.Optional;

public interface RoleService {
    Optional<Roles> findByRoleName(ERole roleName);
}
