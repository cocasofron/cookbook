package ro.cookbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import ro.cookbook.domain.User;
import ro.cookbook.repositories.UserRepository;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepo;

    @Secured({"ROLE_ADMIN", "ROLE_SUPERUSER"})
    public List<User> getAllUserAccounts() {
        return userRepo.findAll();
    }
}
