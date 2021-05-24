package io.github.xpax.syllabi.controller;

import io.github.xpax.syllabi.entity.User;
import io.github.xpax.syllabi.entity.dto.RoleRequest;
import io.github.xpax.syllabi.entity.dto.UserWithoutPassword;
import io.github.xpax.syllabi.service.UserAccountService;
import io.github.xpax.syllabi.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class UserController {
    private final UserRoleService userRoleService;
    private final UserAccountService userAccountService;

    @Autowired
    public UserController(UserRoleService userRoleService, UserAccountService userAccountService) {
        this.userRoleService = userRoleService;
        this.userAccountService = userAccountService;
    }

    @Secured("ROLE_USER_ADMIN")
    @PostMapping("/users/{userId}/roles")
    public ResponseEntity<User> addRole(@RequestBody RoleRequest role, @PathVariable Integer userId) {
        return new ResponseEntity<>(userRoleService.addRoleForUser(role, userId), HttpStatus.OK);
    }

    @Secured("ROLE_USER_ADMIN")
    @GetMapping("/users")
    public ResponseEntity<Page<UserWithoutPassword>> getAllUsers(@RequestParam Optional<Integer> page,
                                                                 @RequestParam Optional<Integer> size) {
        return new ResponseEntity<>(
                userAccountService.getAllUsers(page.orElse(0), size.orElse(20)),
                HttpStatus.OK
        );
    }

    @Secured("ROLE_USER_ADMIN")
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer userId) {
        userAccountService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
