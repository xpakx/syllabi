package io.github.xpax.syllabi.service;

import io.github.xpax.syllabi.entity.Role;
import io.github.xpax.syllabi.entity.User;
import io.github.xpax.syllabi.entity.dto.RoleRequest;
import io.github.xpax.syllabi.repo.RoleRepository;
import io.github.xpax.syllabi.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;

import java.util.HashSet;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UserRoleServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;

    private UserRoleService userRoleService;

    private RoleRequest roleRequest;
    private Role userAdminrole;
    private User userWithId0;

    private final ProjectionFactory factory = new SpelAwareProxyProjectionFactory();

    @BeforeEach
    void setUp() {
        roleRequest = new RoleRequest();
        roleRequest.setRole("USER_ADMIN");
        userAdminrole = new Role();
        userAdminrole.setAuthority("USER_ADMIN");
        userAdminrole.setId(0);
        userWithId0 = User.builder()
                .id(0)
                .username("marvinminsky")
                .password("oldEncoded")
                .roles(new HashSet<>())
                .build();
    }

    private void injectMocks() {
        userRoleService = new UserRoleService(userRepository, roleRepository);
    }

    @Test
    void shouldGrantUserNewRole() {
        given(roleRepository.findByAuthority("USER_ADMIN"))
                .willReturn(Optional.of(userAdminrole));
        given(userRepository.findById(0))
                .willReturn(Optional.of(userWithId0));
        injectMocks();

        userRoleService.addRoleForUser(roleRequest, 0);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        then(userRepository)
                .should(times(1))
                .save(userCaptor.capture());
        User updatedUser = userCaptor.getValue();

        assertNotNull(updatedUser);
        assertEquals(0, updatedUser.getId());
        assertEquals("marvinminsky", updatedUser.getUsername());
        assertThat(updatedUser.getRoles(), hasItem(hasProperty("authority", equalTo("USER_ADMIN"))));
    }
}
