package io.github.xpax.syllabi.service;

import io.github.xpax.syllabi.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        io.github.xpax.syllabi.entity.User user  = userRepository.findById(Integer.valueOf(username))
                .orElseThrow(() -> new UsernameNotFoundException("No user with id " + username));
        return new User(user.getId().toString(), user.getPassword(), user.getRoles());
    }

    public UserDetails loadUserToLogin(String username) throws UsernameNotFoundException {
        io.github.xpax.syllabi.entity.User user  = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user with name " + username));
        return new User(user.getId().toString(), user.getPassword(), user.getRoles());
    }
}
