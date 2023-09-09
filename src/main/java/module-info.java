//module com.example.c195 {
//    requires javafx.controls;
//    requires javafx.fxml;
//    requires java.sql;
//
//
//    opens com.example.c195 to javafx.fxml;
//    exports com.example.c195;
//    exports com.example.c195.Controller;
//    opens com.example.c195.Controller to javafx.fxml;
//}
module com.example.c195 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.c195 to javafx.fxml;
    opens com.example.c195.Model; // Add this line
    exports com.example.c195;
    exports com.example.c195.Controller;
    opens com.example.c195.Controller to javafx.fxml;
}