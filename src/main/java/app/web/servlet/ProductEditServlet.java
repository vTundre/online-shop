package app.web.servlet;

import app.entity.Product;
import app.service.ProductService;
import app.web.page.PageGenerator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProductEditServlet extends HttpServlet {
    private ProductService productService;

    public ProductEditServlet(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Product product = productService.findById(id);

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("product", product);

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        PageGenerator.generatePage(response.getWriter(), "product_edit.html", pageVariables);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Product product = new Product();
        product.setId(Integer.parseInt(request.getParameter("id")));
        product.setName(request.getParameter("name"));
        product.setDescription(request.getParameter("description"));
        product.setPrice(Double.valueOf(request.getParameter("price")));

        productService.update(product);

        response.sendRedirect("/products");
    }

}
