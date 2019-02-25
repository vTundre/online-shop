package app.web.controller;

import app.service.SecurityService;
import app.web.PageGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AdminController {
    @Autowired
    private SecurityService securityService;

    @PostMapping(path = "login")
    protected void login(@RequestParam String login, @RequestParam String password,
                         HttpServletResponse response) throws IOException {
        String token = securityService.getSessionTokenByNameAndPassword(login, password);

        if (!token.isEmpty()) {
            Cookie cookie = new Cookie("user-token", token);
            response.addCookie(cookie);
            response.sendRedirect("/products");
        } else {
            Map<String, Object> pageVariables = new HashMap<>();
            pageVariables.put("message", "Invalid user name or password");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            PageGenerator.generatePage(response.getWriter(), "user_login.html", pageVariables);
        }
    }

    @GetMapping(path = "logout")
    public String logout(@CookieValue(value = "user-token") List<String> token) {
        securityService.deleteSession(token);
        return "redirect:/login";
    }

    @GetMapping(path = "login")
    @ResponseBody
    public String showLogin() {
        return PageGenerator.generatePage("user_login.html");
    }

    @GetMapping(path = "*")
    @ResponseBody
    public String showPageNotFound() {
        return PageGenerator.generatePage("page_not_found.html");
    }
}
