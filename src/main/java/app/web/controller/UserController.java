package app.web.controller;

import app.entity.User;
import app.entity.UserRole;
import app.service.SecurityService;
import app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @GetMapping(path = "user/add")
    public String showAddUser() {
        return "user_add";
    }

    @PostMapping(path = "user/add")
    public String addUser(@RequestParam String login, @RequestParam String password, ModelMap model) {
        if (userService.getByName(login) == null) {
            User user = new User();
            user.setName(login);
            user.setPassword(securityService.getPasswordService().getSaltedHash(password.toLowerCase()));
            user.setRole(UserRole.USER.getName());
            userService.insert(user);

            return "redirect:/login";
        } else {
            model.addAttribute("message", "Such user already registered. Please try to login");
            return "user_add";
        }
    }
}
