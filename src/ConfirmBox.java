import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBox {

    static boolean answer;

    public static boolean display() {
        Stage window = new Stage();
        window.setResizable(false);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Checkers - exit confirmation");
        window.setMinWidth(250);
        Label label1 = new Label();
        label1.setStyle("-fx-text-fill: white; -fx-font-size:24");
        label1.setText("Are You sure to exit? Make sure You've saved the game.");

        Button yesButton = new Button("Yes");
        Button noButton = new Button("No");

        yesButton.setOnAction(event -> {
            answer = true;
            window.close();
        });

        noButton.setOnAction(event -> {
            answer = false;
            window.close();
        });

        VBox confirmLayout = new VBox(10);

        Image confirmBackground = new Image("file:resources/background_wood.jpg");
        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(confirmBackground, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);
        confirmLayout.setBackground(background);

        confirmLayout.getChildren().addAll(label1, yesButton, noButton);
        confirmLayout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(confirmLayout);
        window.setScene(scene);
        window.showAndWait();

        return answer;
    }
}
