package com.bezkoder.springjwt.repository;

import com.bezkoder.springjwt.models.ERole;
import com.bezkoder.springjwt.models.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class InitialDbData {
    private final RoleRepository roleRepository;

    @Autowired
    public InitialDbData(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
        loadUsers();
    }

    @Transactional
    void loadUsers() {
        List<Role> roles = roleRepository.findAll();
        boolean hasRoleUser = roles.stream().anyMatch((role) -> role.getName() == ERole.ROLE_USER);
        boolean hasRoleModerator = roles.stream().anyMatch((role) -> role.getName() == ERole.ROLE_MODERATOR);
        boolean hasRoleAdmin = roles.stream().anyMatch((role) -> role.getName() == ERole.ROLE_ADMIN);
        if (!hasRoleUser) {
            roleRepository.save(new Role(ERole.ROLE_USER));
        }
        if (!hasRoleModerator) {
            roleRepository.save(new Role(ERole.ROLE_MODERATOR));
        }
        if (!hasRoleAdmin) {
            roleRepository.save(new Role(ERole.ROLE_ADMIN));
        }
    }
}
