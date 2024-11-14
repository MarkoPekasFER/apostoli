package com.apostoli.UnluckyApp.service.impl;

import com.apostoli.UnluckyApp.model.entity.Role;
import com.apostoli.UnluckyApp.model.enums.RoleType;
import com.apostoli.UnluckyApp.repository.RoleRepository;
import com.apostoli.UnluckyApp.service.RoleService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role findByName(RoleType roleType) {
        return roleRepository.findByName(roleType)
                .orElseThrow(() -> new IllegalStateException("Role not found"));
    }

    @Override
    public void createRoleIfNotFound(RoleType roleType) {
        if (roleRepository.findByName(roleType).isEmpty()) {
            Role role = new Role();
            role.setName(roleType);
            roleRepository.save(role);
        }
    }

    @Override
    @PostConstruct
    public void initializeRoles() {
        for (RoleType roleType : RoleType.values()) {
            createRoleIfNotFound(roleType);
        }
    }
}