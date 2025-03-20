package DataBase.com.example.Commands.Model;

import java.util.Map;

public class CreateTableRequest
{
    private String dbName;
    private String tableName;
    private Map<String, String> fields;

    // setter and getter
    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Map<String, String> getFields() {
        return fields;
    }

    public void setFields(Map<String, String> fields) {
        this.fields = fields;
    }
}
