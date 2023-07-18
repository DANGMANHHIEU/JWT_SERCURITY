package com.example.sercurity.model.seviceImp;

import com.example.sercurity.model.entity.ERole;
import com.example.sercurity.model.entity.Roles;
import com.example.sercurity.model.repository.RoleRepository;
import com.example.sercurity.model.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class RoleServiceImp implements RoleService {
    @Autowired
private RoleRepository roleRepository;
    @Override
    public Optional<Roles> findByRoleName(ERole roleName) {
        return roleRepository.findByRoleName(roleName);
    }
}
