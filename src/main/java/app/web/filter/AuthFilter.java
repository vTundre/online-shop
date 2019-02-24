package app.web.filter;


import app.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(1)
public class AuthFilter extends GenericFilterBean {

    @Autowired
    @Qualifier("securityServiceBean")
    private SecurityService securityService;

    @Override
    @GetMapping(path = "/*")
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        System.out.println("vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");

        if (securityService.hasAccess(httpServletRequest.getCookies())) {
            chain.doFilter(request, response);
        } else {
            httpServletResponse.sendRedirect("login");
        }
    }

}
