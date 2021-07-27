package io.github.xpax.syllabi.service;

import io.github.xpax.syllabi.entity.User;
import io.github.xpax.syllabi.entity.dto.ChangePasswordRequest;
import io.github.xpax.syllabi.entity.dto.UserWithoutPassword;
import io.github.xpax.syllabi.error.NotFoundException;
import io.github.xpax.syllabi.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;

@Service
public class UserAccountService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserAccountService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Page<UserWithoutPassword> getAllUsers(Integer page, Integer size) {
        return userRepository.findAllProjectedBy(PageRequest.of(page, size));
    }

    public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
    }

    public UserWithoutPassword getUser(Integer userId) {
        return userRepository.findAllProjectedById(userId)
                .orElseThrow(() -> new NotFoundException("User with id "+userId+" not found!"));
    }

    public void changePassword(ChangePasswordRequest request, Integer userId) {
        if(!request.getPassword().equals(request.getPasswordRe())) {
            throw new ValidationException("Passwords don't match!");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id "+userId+" not found!"));
        if(!passwordEncoder.matches(request.getPasswordOld(), user.getPassword())) {
            throw new ValidationException("Wrong old password!");
        }

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }
}
