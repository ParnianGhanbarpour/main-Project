package reception.model.utils;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class JdbcProvider {
    private static JdbcProvider instance;
    private BasicDataSource basicDataSource = new BasicDataSource();

    private JdbcProvider() {
    }
    public static JdbcProvider getInstance() {
        if (instance == null) {
            instance = new JdbcProvider();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        basicDataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        basicDataSource.setUrl("jdbc:oracle:thin:@localhost:1521:xe");
        basicDataSource.setUsername("javaProject");
        basicDataSource.setPassword("java123");
        basicDataSource.setMinIdle(5);
        basicDataSource.setMaxIdle(20);
        return basicDataSource.getConnection();
    }

}
