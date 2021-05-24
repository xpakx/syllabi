package io.github.xpax.syllabi.service;

import io.github.xpax.syllabi.entity.Role;
import io.github.xpax.syllabi.entity.User;
import io.github.xpax.syllabi.entity.dto.RoleRequest;
import io.github.xpax.syllabi.error.NotFoundException;
import io.github.xpax.syllabi.repo.RoleRepository;
import io.github.xpax.syllabi.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRoleService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserRoleService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public User addRoleForUser(RoleRequest role, Integer userId) {
        Role roleToAdd = roleRepository.findByAuthority(role.getRole()).orElse(
                Role.builder()
                        .authority(role.getRole())
                        .build());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id "+userId+" not found!"));
        user.getRoles().add(roleToAdd);

        return userRepository.save(user);
    }

}
