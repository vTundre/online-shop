package app.web.filter;

import app.entity.UserRole;
import app.service.SecurityService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class RoleFilter implements Filter {

    private SecurityService securityService;
    private UserRole userRole;

    public RoleFilter(UserRole userRole) {
        this.userRole = userRole;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

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


}