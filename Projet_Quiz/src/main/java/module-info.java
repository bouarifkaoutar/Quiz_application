module com.example.projet_quiz {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.projet_quiz to javafx.fxml;
    exports com.example.projet_quiz;
}