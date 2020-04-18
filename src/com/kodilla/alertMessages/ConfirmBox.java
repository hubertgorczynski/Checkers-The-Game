package com.kodilla.alertMessages;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBox {

    private boolean answer;

    public boolean display() {
        Stage stage = new Stage();
        stage.setTitle("Checkers - exit confirmation");
        stage.setWidth(500);
        stage.setHeight(200);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);

        Scene confirmScene = new Scene(buildLayout(stage));
        stage.setScene(confirmScene);
        stage.showAndWait();

        return answer;
    }

    private VBox buildLayout(Stage stage) {
        VBox layout = new VBox(10);

        Image confirmBackground = new Image("file:resources/background_wood.jpg");
        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true,
                true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(confirmBackground, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);
        layout.setBackground(background);
        layout.setAlignment(Pos.CENTER);

        Label areYouSureToExit = new Label();
        areYouSureToExit.setStyle("-fx-text-fill: white; -fx-font-size:24");
        areYouSureToExit.setText("Are You sure to exit?");

        Button yesButton = createYesButton(stage);
        Button noButton = createNoButton(stage);
        layout.getChildren().addAll(areYouSureToExit, yesButton, noButton);

        return layout;
    }

    private Button createYesButton(Stage stage) {
        Button yesButton = new Button("Yes");
        yesButton.setOnAction(event -> {
            answer = true;
            stage.close();
        });
        return yesButton;
    }

    private Button createNoButton(Stage stage) {
        Button noButton = new Button("No");
        noButton.setOnAction(event -> {
            answer = false;
            stage.close();
        });
        return noButton;
    }
}
