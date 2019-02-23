package app.web.servlet;

import app.entity.Product;
import app.service.ProductService;
import app.web.page.PageGenerator;
import service.ServiceLocator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProductAddServlet extends HttpServlet {
    private ProductService productService;

    public ProductAddServlet() {
        this.productService = ServiceLocator.getService(ProductService.class);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        PageGenerator.generatePage(response.getWriter(), "product_add.html");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Product product = new Product();
        product.setName(request.getParameter("name"));
        product.setDescription(request.getParameter("description"));
        product.setPrice(Double.valueOf(request.getParameter("price")));

        productService.insert(product);

        response.sendRedirect("../products");
    }
}
