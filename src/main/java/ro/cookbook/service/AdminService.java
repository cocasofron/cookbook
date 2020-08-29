package ro.cookbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import ro.cookbook.domain.Authorities;
import ro.cookbook.domain.User;
import ro.cookbook.repositories.UserRepository;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepo;

    @Secured({"ROLE_ADMIN", "ROLE_SUPERUSER"})
    public List<User> getAllUserAccounts() {
        return userRepo.findAll();
    }

    public User changeRole(User user, String role) {
        user.setAuthorities(new HashSet<>(Collections.singletonList(new Authorities(role, user))));
        return userRepo.save(user);
    }

    public void deleteUser(User user) {
        userRepo.delete(user);
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public User findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public Set<User> searchUser(String search) {
        return userRepo.findAll().stream()
                .filter(user -> user.getUsername().equalsIgnoreCase(search) ||
                        user.getUsername().contains(search) ||
                        user.getFirstName().equalsIgnoreCase(search) ||
                        user.getLastName().equalsIgnoreCase(search))
                .collect(Collectors.toSet());
    }
}
