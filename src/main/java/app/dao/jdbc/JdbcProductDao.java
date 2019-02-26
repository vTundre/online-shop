package app.dao.jdbc;

import app.dao.ProductDao;
import app.dao.jdbc.mapper.ProductMapper;
import app.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class JdbcProductDao implements ProductDao {
    private static final String SQL_SELECT_ALL = "select id, name, description, price from product order by id";
    private static final String SQL_SELECT_BY_ID = "select id, name, description, price from product where id = ?";
    private static final String SQL_DELETE_BY_ID = "delete from product where id = ?";
    private static final String SQL_INSERT = "insert into product(id, name, description, price) values (nextval('product_seq'),?, ?, ?)";
    private static final String SQL_UPDATE_BY_ID = "update product set name = ?, description = ?, price = ? where id = ?";

    @Autowired
    private DataSource dataSource;

    @Override
    public List<Product> findAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<Product> products = jdbcTemplate.query(SQL_SELECT_ALL, new ProductMapper());
        return products;
    }

    @Override
    public Product findById(int id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<Product> products = jdbcTemplate.query(SQL_SELECT_BY_ID, new ProductMapper(), id);
        return products.size() == 0 ? null : products.get(0);
    }

    @Override
    public void insert(Product product) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update(SQL_INSERT, product.getName(), product.getDescription(), product.getPrice());
    }

    @Override
    public void deleteById(int id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update(SQL_DELETE_BY_ID, id);
    }

    @Override
    public void update(Product product) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update(SQL_UPDATE_BY_ID, product.getName(), product.getDescription(),
                product.getPrice(), product.getId());
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
