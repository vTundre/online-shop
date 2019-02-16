package app.dao.jdbc.mapper;

import app.entity.Product;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductMapper {
    public static Product mapRow(ResultSet resultSet) throws SQLException {
        Product product = new Product();
        product.setId(resultSet.getInt("id"));
        product.setName(resultSet.getString("name"));
        product.setDescription(resultSet.getString("description"));
        product.setPrice(resultSet.getDouble("price"));
        return product;
    }
}