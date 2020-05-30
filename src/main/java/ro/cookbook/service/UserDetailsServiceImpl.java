package ro.cookbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ro.cookbook.domain.Authorities;
import ro.cookbook.domain.User;
import ro.cookbook.repositories.UserRepository;
import ro.cookbook.security.CustomSecurityUser;

import java.util.Collections;
import java.util.HashSet;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String DEFAULT_USER_ROLE = "ROLE_USER";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepo.findByUsername(username);

        if (user == null)
            throw new UsernameNotFoundException("Username and or password was incorrect.");

        return new CustomSecurityUser(user);
    }

    public User registrer(User user) {
        user.setAuthorities(new HashSet<>(Collections.singletonList(new Authorities(DEFAULT_USER_ROLE, user))));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }
}
