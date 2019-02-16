package app.dao;

import app.entity.Product;

import java.util.List;

public interface ProductDao {
    List<Product> findAll();

    Product findById(int id);

    void insert(Product user);

    void deleteById(int id);

    void update(Product user);
}
