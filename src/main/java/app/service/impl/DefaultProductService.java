package app.service.impl;

import app.dao.ProductDao;
import app.entity.Product;
import app.service.ProductService;

import java.util.List;

public class DefaultProductService implements ProductService{

    private ProductDao productDao;

    public DefaultProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    public List<Product> findAll() {
        return productDao.findAll();
    }

    @Override
    public Product findById(int id) {
        return productDao.findById(id);
    }

    @Override
    public void insert(Product product) {
        productDao.insert(product);
    }

    @Override
    public void deleteById(int id) {
        productDao.deleteById(id);
    }

    @Override
    public void update(Product product) {
        productDao.update(product);
    }
}
