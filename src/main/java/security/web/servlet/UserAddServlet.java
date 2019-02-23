package security.web.servlet;

import app.entity.User;
import app.entity.UserRole;
import security.service.SecurityService;
import app.service.UserService;
import app.web.page.PageGenerator;
import service.ServiceLocator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserAddServlet extends HttpServlet {
    private UserService userService;
    private SecurityService securityService;

    public UserAddServlet() {
        this.userService = ServiceLocator.getService(UserService.class);
        this.securityService = ServiceLocator.getService(SecurityService.class);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        PageGenerator.generatePage(response.getWriter(), "user_add.html");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String login = request.getParameter("login").toLowerCase();
        String password = request.getParameter("password");

        if (userService.getByName(login) == null) {
            User user = new User();
            user.setName(login);
            user.setPassword(securityService.getPasswordService().getSaltedHash(password));
            user.setRole(UserRole.USER.getName());
            userService.insert(user);
            response.sendRedirect("../login");
        } else {
            Map<String, Object> pageVariables = new HashMap<>();
            pageVariables.put("message", "Such user already registered. Please try to login");

            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            PageGenerator.generatePage(response.getWriter(), "user_add.html", pageVariables);
        }
    }
}
