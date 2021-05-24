package io.github.xpax.syllabi.service;

import io.github.xpax.syllabi.entity.dto.UserWithoutPassword;
import io.github.xpax.syllabi.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class UserAccountService {
    private final UserRepository userRepository;

    @Autowired
    public UserAccountService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Page<UserWithoutPassword> getAllUsers(Integer page, Integer size) {
        return userRepository.findAllProjectedBy(PageRequest.of(page, size));
    }

    public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
    }
}
