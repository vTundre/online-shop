package app.web.controller;

import app.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class AdminController {
    @Autowired
    private SecurityService securityService;

    @PostMapping(path = "login")
    protected String login(@RequestParam String login, @RequestParam String password,
                           HttpServletResponse response, ModelMap model) {
        String token = securityService.getSessionTokenByNameAndPassword(login, password);

        if (!token.isEmpty()) {
            Cookie cookie = new Cookie("user-token", token);
            response.addCookie(cookie);
            return "redirect:/products";
        } else {
            model.addAttribute("message", "Invalid user name or password");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return "user_login";
        }
    }

    @GetMapping(path = "logout")
    public String logout(@CookieValue(value = "user-token") List<String> tokens) {
        securityService.deleteSession(tokens);
        return "redirect:/login";
    }

    @GetMapping(path = "login")
    public String showLogin() {
        return "user_login";
    }

    @GetMapping(path = "*")
    public String showPageNotFound() {
        return "page_not_found";
    }
}
