package app.web.controller;

import app.entity.Product;
import app.entity.UserRole;
import app.service.ProductService;
import app.service.SecurityService;
import app.web.PageGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private SecurityService securityService;

    @GetMapping(path = "product/add")
    @ResponseBody
    public String showAddProduct() {
        return PageGenerator.generatePage("product_add.html");
    }


    @GetMapping("product/edit")
    @ResponseBody
    public String showEditUser(@RequestParam int id) {
        Product product = productService.findById(id);

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("product", product);
        return PageGenerator.generatePage("product_edit.html", pageVariables);
    }


    @GetMapping(path = "products")
    @ResponseBody
    public String showAllProducts(@CookieValue(value = "user-token") List<String> token) {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("products", productService.findAll());

        String templateName = "product_show.html";
        if (securityService.hasRoleAccess(token, UserRole.ADMIN)) {
            templateName = "product_show_all.html";
        }
        return PageGenerator.generatePage(templateName, pageVariables);
    }


    @PostMapping(path = "product/add")
    public String addProduct(@RequestParam String name, @RequestParam String description, @RequestParam Double price) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);

        productService.insert(product);

        return "redirect:/products";
    }


    @PostMapping(path = "product/delete")
    public String deleteProduct(@RequestParam int id) {
        productService.deleteById(id);
        return "redirect:/products";
    }


    @PostMapping("product/edit")
    public String editProduct(@RequestParam int id, @RequestParam String name,
                              @RequestParam String description, @RequestParam double price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);

        productService.update(product);

        return "redirect:/products";
    }
}
