package app.web.servlet;

import app.entity.UserRole;
import security.service.SecurityService;
import app.service.ProductService;
import app.web.page.PageGenerator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProductShowServlet extends HttpServlet {
    private ProductService productService;
    private SecurityService securityService;

    public ProductShowServlet(ProductService productService, SecurityService securityService) {
        this.productService = productService;
        this.securityService = securityService;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("products", productService.findAll());

        String templateName = "product_show.html";
        if (securityService.hasRoleAccess(request.getCookies(), UserRole.ADMIN)) {
            templateName = "product_show_all.html";
        }

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        PageGenerator.generatePage(response.getWriter(), templateName, pageVariables);
    }
}
