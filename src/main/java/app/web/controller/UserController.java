package app.web.controller;

import app.entity.User;
import app.entity.UserRole;
import app.service.SecurityService;
import app.service.UserService;
import app.web.PageGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @GetMapping(path = "user/add")
    @ResponseBody
    public String showAddUser() {
        return PageGenerator.generatePage("user_add.html");
    }

    @PostMapping(path = "user/add")
    public void addUser(@RequestParam String login, @RequestParam String password, HttpServletResponse response) throws IOException {
        if (userService.getByName(login) == null) {
            User user = new User();
            user.setName(login);
            user.setPassword(securityService.getPasswordService().getSaltedHash(password.toLowerCase()));
            user.setRole(UserRole.USER.getName());
            userService.insert(user);

            response.sendRedirect("/login");
        } else {
            Map<String, Object> pageVariables = new HashMap<>();
            pageVariables.put("message", "Such user already registered. Please try to login");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            PageGenerator.generatePage(response.getWriter(), "user_add.html", pageVariables);
        }
    }
}
