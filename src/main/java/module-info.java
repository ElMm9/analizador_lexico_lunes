module com.example.analizador_lexico {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.analizador_lexico to javafx.fxml;
    exports com.example.analizador_lexico;
}