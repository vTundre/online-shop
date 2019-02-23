package security.web.servlet;

import security.service.SecurityService;
import service.ServiceLocator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogoutServlet extends HttpServlet {

    private SecurityService securityService;

    public LogoutServlet() {
        this.securityService = ServiceLocator.getService(SecurityService.class);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        securityService.deleteSession(request.getCookies());
        response.sendRedirect("login");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }
}
