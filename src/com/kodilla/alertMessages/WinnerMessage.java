package com.kodilla.alertMessages;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class WinnerMessage {

    private static boolean answer;

    public static boolean display(String winner) {
        Stage stage = new Stage();
        stage.setTitle("Checkers - Congratulation!");
        stage.setWidth(650);
        stage.setHeight(200);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);

        Scene winnerScene = new Scene(buildLayout(stage, winner));
        stage.setScene(winnerScene);
        stage.showAndWait();

        return answer;
    }

    private static VBox buildLayout(Stage stage, String winner) {
        VBox layout = new VBox(10);

        Image congratulationBackground = new Image("file:resources/background_wood.jpg");
        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true,
                true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(congratulationBackground, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);
        layout.setBackground(background);
        layout.setAlignment(Pos.CENTER);

        Label congratulation = new Label();
        congratulation.setStyle("-fx-text-fill: white; -fx-font-size:20");
        congratulation.setText("Congratulation! " + winner + " player wins! Do You want to play again?");

        Button yesButton = createYesButton(stage);
        Button noButton = createNoButton(stage);
        layout.getChildren().addAll(congratulation, yesButton, noButton);

        return layout;
    }

    private static Button createYesButton(Stage stage) {
        Button yesButton = new Button("Yes! (start new game)");
        yesButton.setOnAction(event -> {
            answer = true;
            stage.close();
        });
        return yesButton;
    }

    private static Button createNoButton(Stage stage) {
        Button noButton = new Button("No");
        noButton.setOnAction(event -> {
            answer = false;
            stage.close();
        });
        return noButton;
    }
}
