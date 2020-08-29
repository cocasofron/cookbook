package ro.cookbook.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ro.cookbook.domain.User;
import ro.cookbook.security.UserDetailsServiceImpl;
import ro.cookbook.service.AdminService;

import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private AdminService adminService;

    @GetMapping("/admin")
    public String getAdminPage(@AuthenticationPrincipal User user, Model model) {
        List<User> users = adminService.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("user", user);
        return "admin/adminPage";
    }

    @PostMapping("/admin/changeRole")
    public String changeRole(@AuthenticationPrincipal User user, Model model, String userName, String role) {
        model.addAttribute("user", user);
        model.addAttribute("userName", userName);
        model.addAttribute("role", role);
        User userToChange = adminService.findByUsername(userName);
        adminService.changeRole(userToChange, role);
        return "success";
    }

    @PostMapping("/admin/deleteUser")
    public String deleteUser(@AuthenticationPrincipal User user, Model model, String userName) {
        model.addAttribute("userName", userName);
        User userToDelete = adminService.findByUsername(userName);
        adminService.deleteUser(userToDelete);
        model.addAttribute("user", user);
        return "success";
    }

    @PostMapping("/admin/searchUsers")
    public String searchUsers(@AuthenticationPrincipal User user, Model model, String search) {
        model.addAttribute("search", search);
        model.addAttribute("searchedUsers", adminService.searchUser(search));
        model.addAttribute("user", user);
        return "admin/searchResult";
    }
}
