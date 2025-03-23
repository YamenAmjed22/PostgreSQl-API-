package DataBase.com.example.Commands.Controller;

import DataBase.com.example.Commands.Service.DbManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class DbManagerController {

    private final DbManagerService dbManagerService;

    @Autowired
    public DbManagerController(DbManagerService dbManagerService) {
        this.dbManagerService = dbManagerService;
    }

    @GetMapping("/createDatabase")
    public String showCreateDatabasePage() {
        return "createDatabase";
    }

    @PostMapping("/createDatabase")
    public String createDatabase(@RequestParam String dbName, Model model) {
        try {
            dbManagerService.createDatabase(dbName);
            model.addAttribute("message", "Database " + dbName + " created successfully.");
        } catch (Exception e) {
            model.addAttribute("message", "Error creating database: " + e.getMessage());
        }
        return "createDatabase";
    }

    @GetMapping("/createTable")
    public String showCreateTablePage() {
        return "createTable";
    }

    @PostMapping("/createTable")
    public String createTable(@RequestParam String dbName, @RequestParam String tableName,
                              @RequestParam String fields, Model model) {
        try {
            Map<String, String> fieldMap = parseFields(fields);
            dbManagerService.createTable(dbName, tableName, fieldMap);
            model.addAttribute("message", "Table " + tableName + " created successfully.");
        } catch (Exception e) {
            model.addAttribute("message", "Error creating table: " + e.getMessage());
        }
        return "createTable";
    }

    @GetMapping("/deleteDatabase")
    public String showDeleteDatabasePage() {
        return "deleteDatabase";
    }

    @PostMapping("/deleteDatabase")
    public String deleteDatabase(@RequestParam String dbName, Model model) {
        try {
            dbManagerService.deleteDatabase(dbName);
            model.addAttribute("message", "Database " + dbName + " deleted successfully.");
        } catch (Exception e) {
            model.addAttribute("message", "Error deleting database: " + e.getMessage());
        }
        return "deleteDatabase";
    }

    @GetMapping("/deleteTable")
    public String showDeleteTablePage() {
        return "deleteTable";
    }

    @PostMapping("/deleteTable")
    public String deleteTable(@RequestParam String dbName, @RequestParam String tableName, Model model) {
        try {
            dbManagerService.deleteTable(dbName, tableName);
            model.addAttribute("message", "Table " + tableName + " deleted successfully.");
        } catch (Exception e) {
            model.addAttribute("message", "Error deleting table: " + e.getMessage());
        }
        return "deleteTable";
    }

    private Map<String, String> parseFields(String fields) {
        // Method to parse the "fields" string into a map of column names and types.
        // This is a basic example, and might need improvement based on the actual format.
        return Map.of("id", "INT", "name", "VARCHAR(255)"); // Simplified
    }
}





//    @DeleteMapping("/delete")
//    public String deleteDB(@RequestBody DeleteDataBaseRequest deleteDataBaseRequest) {
//        try {
//            dbManagerService.deleteDatabase(deleteDataBaseRequest.getDbName());
//            return "Database " + deleteDataBaseRequest.getDbName() + " delete successfully.";
//        } catch (Exception e) {
//            return "Error deleting database: " + e.getMessage();
//        }
//    }





