module com.example.a7gui {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;

    opens com.example.a7gui to javafx.fxml;
    exports com.example.a7gui;
}