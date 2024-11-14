package com.apostoli.UnluckyApp.service;

import com.apostoli.UnluckyApp.model.entity.Role;
import com.apostoli.UnluckyApp.model.enums.RoleType;

public interface RoleService {
    Role findByName(RoleType roleType);
    void createRoleIfNotFound(RoleType roleType);
    void initializeRoles();
}