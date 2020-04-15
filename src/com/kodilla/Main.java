package com.kodilla;

import com.kodilla.graphicContent.GUI;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        new GUI(primaryStage);

    }
}
