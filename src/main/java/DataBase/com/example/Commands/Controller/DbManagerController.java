package DataBase.com.example.Commands.Controller;

import DataBase.com.example.Commands.Model.CreateDataBaseRequest;
import DataBase.com.example.Commands.Model.CreateTableRequest;
import DataBase.com.example.Commands.Service.DbManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/DB")
public class DbManagerController {

    private final DbManagerService dbManagerService;

    @Autowired
    public DbManagerController(DbManagerService dbManagerService) {
        this.dbManagerService = dbManagerService;
    }

    // create DB
    @PostMapping("/Create")
    public String CreateDB(@RequestBody CreateDataBaseRequest createDataBaseRequest) {
        try {
            dbManagerService.createDatabase(createDataBaseRequest.getDbName());
            return "Database " + createDataBaseRequest.getDbName() + " created successfully.";
        } catch (Exception e) {
            return "Error creating database: " + e.getMessage();
        }
    }

    @PostMapping("/createTable")
    public String createTable(@RequestBody CreateTableRequest createTableRequest) {
        try {
            // Call the service to create the table in the specified database
            dbManagerService.createTable(createTableRequest.getDbName(), createTableRequest.getTableName(), createTableRequest.getFields());
            return "Table " + createTableRequest.getTableName() + " created successfully in " + createTableRequest.getDbName() + " database.";
        } catch (Exception e) {
            return "Error creating table: " + e.getMessage();
        }
    }


}
