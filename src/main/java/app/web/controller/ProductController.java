package app.web.controller;

import app.entity.Product;
import app.entity.UserRole;
import app.service.ProductService;
import app.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private SecurityService securityService;

    @GetMapping(path = "product/add")
    public String showAddProduct() {
        return "product_add";
    }

    @GetMapping("product/edit")
    public String showEditUser(@RequestParam int id, ModelMap model) {
        Product product = productService.findById(id);

        model.addAttribute("product", product);
        return "product_edit";
    }

    @GetMapping(path = "products")
    public String showAllProducts(@CookieValue(value = "user-token") List<String> token, ModelMap model) {
        model.addAttribute("products", productService.findAll());
        if (securityService.hasRoleAccess(token, UserRole.ADMIN)) {
            return "product_show_all";
        }
        return "product_show";
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
