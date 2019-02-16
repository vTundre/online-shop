package app.web.servlet;

import app.service.ProductService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProductDeleteServlet extends HttpServlet {
    private ProductService productService;

    public ProductDeleteServlet(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");

        productService.deleteById(Integer.parseInt(id));

        response.sendRedirect("/products");
    }
}
