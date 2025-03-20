package DataBase.com.example.Commands.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.Map;

@Service
public class DbManagerService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DbManagerService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // methode to create DB
    public void createDatabase(String dbName) {
        String sql = "CREATE DATABASE " + dbName;
        jdbcTemplate.update(sql);
    }

    // method to create tabel
    public void createTable(String dbName, String tableName, Map<String, String> fields) {
        // Ensure that the schema exists before creating the table
        createSchemaIfNotExists(dbName);

        // Create a dynamic DataSource to connect to the specified database
        DataSource dataSource = createDataSource(dbName);
        JdbcTemplate dynamicJdbcTemplate = new JdbcTemplate(dataSource);

        // Start creating the SQL statement for creating the table
        StringBuilder createTableSql = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
        createTableSql.append(dbName).append(".").append(tableName).append(" (");

        // Add fields to the SQL statement
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            createTableSql.append(entry.getKey()) // column name
                    .append(" ")
                    .append(entry.getValue()) // data type
                    .append(", ");
        }

        // Remove the trailing comma and space after the last column
        if (createTableSql.length() > 0) {
            createTableSql.delete(createTableSql.length() - 2, createTableSql.length());
        }

        // Add closing parenthesis to complete the table definition
        createTableSql.append(")");

        // Print the final SQL query for debugging purposes (optional)
        System.out.println("Generated SQL Query: " + createTableSql.toString());

        // Execute the SQL statement
        try {
            dynamicJdbcTemplate.execute(createTableSql.toString());
            System.out.println("Table " + tableName + " created successfully in " + dbName + " database.");
        } catch (Exception e) {
            System.err.println("Error executing SQL: " + e.getMessage());
            throw new RuntimeException("Error executing table creation: " + e.getMessage());
        }
    }

    private void createSchemaIfNotExists(String dbName) {
        // Create a dynamic DataSource to connect to the database
        DataSource dataSource = createDataSource(dbName);
        JdbcTemplate dynamicJdbcTemplate = new JdbcTemplate(dataSource);

        // SQL to create schema if it doesn't exist
        String schemaCheckSql = "CREATE SCHEMA IF NOT EXISTS " + dbName;

        try {
            dynamicJdbcTemplate.execute(schemaCheckSql);
            System.out.println("Schema " + dbName + " checked/created.");
        } catch (Exception e) {
            System.err.println("Error creating schema: " + e.getMessage());
            throw new RuntimeException("Error creating schema: " + e.getMessage());
        }
    }

    // Create a new DataSource for the specified database
    private DataSource createDataSource(String dbName) {
        String dbUrl = "jdbc:postgresql://localhost:5432/" + dbName;
        System.out.println("Connecting to URL: " + dbUrl);

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(dbUrl);
        dataSource.setUsername("postgres");  // Set the appropriate username
        dataSource.setPassword("admin");  // Set the appropriate password
        return dataSource;
    }


}
