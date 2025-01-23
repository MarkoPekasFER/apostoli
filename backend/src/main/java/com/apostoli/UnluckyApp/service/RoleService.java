package com.apostoli.UnluckyApp.service;

import com.apostoli.UnluckyApp.model.entity.Role;
import com.apostoli.UnluckyApp.model.enums.RoleType;
import jakarta.annotation.PostConstruct;

public interface RoleService {
    Role findByName(RoleType roleType);

    void createRoleIfNotFound(RoleType roleType);

    @PostConstruct
    void initializeRoles();
}
