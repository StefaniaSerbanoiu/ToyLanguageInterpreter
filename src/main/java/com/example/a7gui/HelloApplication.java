package com.example.a7gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;


public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader programListLoader = new FXMLLoader();
        programListLoader.setLocation(HelloApplication.class.getResource("chooseProgram.fxml"));
        Parent programListRoot = programListLoader.load();
        Scene programListScene = new Scene(programListRoot, 875, 502);

        chooseProgram programChooserController = programListLoader.getController();
        stage.setTitle("Select a program");
        stage.setScene(programListScene);
        stage.show();

        FXMLLoader programExecutorLoader = new FXMLLoader();
        programExecutorLoader.setLocation(HelloApplication.class.getResource("ProgramExecutorController.fxml"));
        Parent programExecutorRoot = programExecutorLoader.load();
        Scene programExecutorScene = new Scene(programExecutorRoot, 700, 500);

        ProgramExecutorController programExecutorController = programExecutorLoader.getController();
        programChooserController.setExec(programExecutorController);
        Stage secondaryStage = new Stage();
        secondaryStage.setTitle("Interpreter");
        secondaryStage.setScene(programExecutorScene);
        secondaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
