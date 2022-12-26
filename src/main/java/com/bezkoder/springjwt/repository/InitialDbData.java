package com.bezkoder.springjwt.repository;

import com.bezkoder.springjwt.models.ERole;
import com.bezkoder.springjwt.models.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InitialDbData {
    private final RoleRepository roleRepository;

    @Autowired
    public InitialDbData(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
        loadUsers();
    }

    private void loadUsers() {
        roleRepository.save(new Role(ERole.ROLE_USER));
        roleRepository.save(new Role(ERole.ROLE_MODERATOR));
        roleRepository.save(new Role(ERole.ROLE_ADMIN));
    }
}
