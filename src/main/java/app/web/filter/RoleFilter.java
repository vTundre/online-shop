package app.web.filter;

import app.entity.UserRole;
import app.service.SecurityService;
import app.web.controller.UserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public abstract class RoleFilter implements Filter {
    @Autowired
    @Qualifier("securityServiceBean")
    private SecurityService securityService;

    private UserRole userRole;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, request.getServletContext());
        if (securityService.hasRoleAccess(httpServletRequest.getCookies(), userRole)) {
            chain.doFilter(request, response);
        } else {
            httpServletResponse.sendRedirect("login");
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }

    public SecurityService getSecurityService() {
        return securityService;
    }

    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }
}