package io.github.xpax.syllabi.controller;

import io.github.xpax.syllabi.entity.User;
import io.github.xpax.syllabi.entity.dto.RoleRequest;
import io.github.xpax.syllabi.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final UserRoleService userRoleService;

    @Autowired
    public UserController(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @Secured("ROLE_USER_ADMIN")
    @PostMapping("/users/{userId}/roles")
    public ResponseEntity<User> addRole(@RequestBody RoleRequest role, @PathVariable Integer userId) {
        return new ResponseEntity<>(userRoleService.addRoleForUser(role, userId), HttpStatus.OK);
    }

}
