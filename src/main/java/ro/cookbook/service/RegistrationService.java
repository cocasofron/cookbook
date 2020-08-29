package ro.cookbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ro.cookbook.domain.Authorities;
import ro.cookbook.domain.User;
import ro.cookbook.repositories.UserRepository;

import java.util.Collections;
import java.util.HashSet;

@Service
public class RegistrationService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String DEFAULT_USER_ROLE = "ROLE_USER";

    public User registrer(User user) {
        user.setAuthorities(new HashSet<>(Collections.singletonList(new Authorities(DEFAULT_USER_ROLE, user))));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }
}
