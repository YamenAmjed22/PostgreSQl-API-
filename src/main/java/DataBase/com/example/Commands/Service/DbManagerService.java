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

    // method to create table
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

    // delete data base
    public void deleteDatabase(String dbName) {
        // Query to terminate connections
        String terminateQuery = "SELECT pg_terminate_backend(pid) " +
                "FROM pg_stat_activity " +
                "WHERE datname = ? " +  // Use placeholder for dynamic value
                "AND pid <> pg_backend_pid()";

        // Execute termination query using jdbcTemplate.query() to get results (if any)
        jdbcTemplate.query(terminateQuery, new Object[]{dbName}, (rs) -> {
            // You don't need to do anything with the results, just execute
            return null;
        });

        // Query to drop the database
        String dropQuery = "DROP DATABASE IF EXISTS " + dbName;  // Be cautious here: SQL injection risk if dbName is not sanitized.

        // Execute drop query
        jdbcTemplate.update(dropQuery);
    }

    public void dropTable(String dbName, String tableName) {
        // Ensure that the schema exists before dropping the table (optional)
        // You could add a check here for schema existence if needed, though dropping tables is schema-specific.
        // createSchemaIfNotExists(dbName);

        // Create a dynamic DataSource to connect to the specified database
        DataSource dataSource = createDataSource(dbName);
        JdbcTemplate dynamicJdbcTemplate = new JdbcTemplate(dataSource);

        // Build the SQL query to drop the table
        String dropTableSql = "DROP TABLE IF EXISTS " + dbName + "." + tableName;

        // Print the final SQL query for debugging purposes (optional)
        System.out.println("Generated SQL Query: " + dropTableSql);

        // Execute the SQL statement to drop the table
        try {
            dynamicJdbcTemplate.execute(dropTableSql);
            System.out.println("Table " + tableName + " dropped successfully from " + dbName + " database.");
        } catch (Exception e) {
            System.err.println("Error executing SQL: " + e.getMessage());
            throw new RuntimeException("Error executing table deletion: " + e.getMessage());
        }
    }
    public void deleteTable(String dataBaseName, String tableName) {
        DataSource dataSource = createDataSource(dataBaseName);
        JdbcTemplate dynamicJdbcTemplate = new JdbcTemplate(dataSource);

        // Build the SQL query to drop the table
        String dropTableSql = "DROP TABLE IF EXISTS " + dataBaseName + "." + tableName;

        // Print the final SQL query for debugging purposes (optional)
        System.out.println("Generated SQL Query: " + dropTableSql);

        // Execute the SQL statement to drop the table
        try {
            dynamicJdbcTemplate.execute(dropTableSql);
            System.out.println("Table " + tableName + " dropped successfully from " + dataBaseName + " database.");
        } catch (Exception e) {
            System.err.println("Error executing SQL: " + e.getMessage());
            throw new RuntimeException("Error executing table deletion: " + e.getMessage());
        }
    }


    // create Schema
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
