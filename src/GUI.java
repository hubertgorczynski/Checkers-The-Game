import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.net.URI;

public class GUI {

    public static final String GAME_PREAMBLE_AND_INSTRUCTIONS = "           --- Welcome in the checkers game!!! ---\n" +
            "\nColors interpretation:\n" +
            "- Green highlighted squares marked pawns that You can choose.\n" +
            "- Blue highlighted squares marked tiles where You can go.\n" +
            "- Red highlighted squares marked mandatory kills.\n" +
            "---------------------------------------------------\n";
    public static TextArea output;
    private Game game;

    public GUI(Stage primaryStage) {
        configureApplicationWindow(primaryStage);
        initialiseApplicationBackend();
        Scene GUI = new Scene(createGUI());
        primaryStage.setScene(GUI);

        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            closeProgram(primaryStage);
        });

        primaryStage.show();
    }

    private void closeProgram(Stage primaryStage) {
        boolean answer = ConfirmBox.display();
        if (answer)
            primaryStage.close();
    }

    private void configureApplicationWindow(Stage primaryStage) {
        primaryStage.setTitle("Checkers - game");
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNIFIED);
    }

    private void initialiseApplicationBackend() {
        Player initialBlackPlayer = new HumanPlayer(Team.BLACK);
        Player initialWhitePlayer = new HumanPlayer(Team.WHITE);
        game = new Game(initialBlackPlayer, initialWhitePlayer);
    }

    private Parent createGUI() {
        Image imageBack = new Image("file:resources/background_wood.jpg");
        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO,
                true, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(imageBack, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background woodenTexture = new Background(backgroundImage);

        VBox controls = buildControls();
        Pane gameBoard = getGameBoard();
        setUpGameOutputFeed();

        HBox layout = new HBox(10, controls, gameBoard, output);
        layout.setPadding(new Insets(10));
        layout.setBackground(woodenTexture);

        return layout;
    }

    private Pane getGameBoard() {
        Pane gameBoard = new Pane();
        int squareEdgeLength = Game.BOARD_SIZE * Game.TILE_SIZE;
        gameBoard.setMinSize(squareEdgeLength, squareEdgeLength);
        gameBoard.getChildren().setAll(game.getComponents());

        return gameBoard;
    }

    private VBox buildControls() {
        Button newGameButton = getNewGameButton();
        Button saveGameButton = getSaveGameButton();
        Button loadGameButton = getLoadGameButton();
        Button userMoveHighlightingToggleButton = getUserMoveHighlightingToggleButton();
        Button AIMoveHighlightingToggleButton = getAIMoveHighlightingToggleButton();
        Button displayInstructionsButton = getDisplayInstructionsButton();
        VBox playerControls = getPlayerControls();

        VBox userControls = new VBox(10, newGameButton, saveGameButton, loadGameButton,
                userMoveHighlightingToggleButton, AIMoveHighlightingToggleButton, displayInstructionsButton, playerControls);

        userControls.setPrefWidth(300);
        userControls.setMinWidth(300);

        return userControls;
    }

    private VBox getPlayerControls() {
        ComboBox<String> blackPlayer = getPlayerMenu(Team.BLACK);
        ComboBox<String> whitePlayer = getPlayerMenu(Team.WHITE);

        GridPane teamPlayerMenus = new GridPane();

        Label blackPlayerLabel = new Label("Black player");
        blackPlayerLabel.setTextFill(Color.valueOf("white"));
        teamPlayerMenus.add(blackPlayerLabel, 0, 0);
        teamPlayerMenus.add(blackPlayer, 1, 0);

        Label whitePlayerLabel = new Label("White player");
        whitePlayerLabel.setTextFill(Color.valueOf("white"));
        teamPlayerMenus.add(whitePlayerLabel, 0, 1);
        teamPlayerMenus.add(whitePlayer, 1, 1);
        teamPlayerMenus.setHgap(10);

        return new VBox(10, teamPlayerMenus);
    }

    private ComboBox<String> getPlayerMenu(Team team) {
        ComboBox<String> playerMenu = new ComboBox<>();
        playerMenu.getItems().setAll("Human", "Computer");
        playerMenu.getSelectionModel().select("Human");
        playerMenu.setOnAction((event -> {
            switch (playerMenu.getSelectionModel().getSelectedIndex()) {
                case 0:
                    game.restartGame(new HumanPlayer(team));
                    break;
                case 1:
                    game.restartGame(new RandomAIPlayer(team));
                    break;
            }
        }));
        return playerMenu;
    }

    private Button getUserMoveHighlightingToggleButton() {
        String mechanism = "user move highlighting";
        Button userMoveHighlightingToggleButton = new Button("Disable " + mechanism + "\n");

        userMoveHighlightingToggleButton.setOnAction(value -> {
            game.toggleUserMoveHighlighting();
            if (Game.USER_MOVE_HIGHLIGHTING) {
                userMoveHighlightingToggleButton.setText("Disable " + mechanism + "\n");
                output.appendText(mechanism + " enabled\n");
            } else {
                userMoveHighlightingToggleButton.setText("Enable " + mechanism + "\n");
                output.appendText(mechanism + " disabled\n");
            }
        });

        userMoveHighlightingToggleButton.setMaxWidth(Double.MAX_VALUE);
        return userMoveHighlightingToggleButton;
    }

    private Button getAIMoveHighlightingToggleButton() {
        String mechanism = "computer moves highlighting";
        Button AIMoveHighlightingToggleButton = new Button("Disable " + mechanism + "\n");

        AIMoveHighlightingToggleButton.setOnAction(value -> {
            Game.AI_MOVE_HIGHLIGHTING = !Game.AI_MOVE_HIGHLIGHTING;
            if (Game.AI_MOVE_HIGHLIGHTING) {
                AIMoveHighlightingToggleButton.setText("Disable " + mechanism + "\n");
                output.appendText(mechanism + " enabled\n");
            } else {
                AIMoveHighlightingToggleButton.setText("Enable " + mechanism + "\n");
                output.appendText(mechanism + " disabled\n");
            }
        });

        AIMoveHighlightingToggleButton.setMaxWidth(Double.MAX_VALUE);
        return AIMoveHighlightingToggleButton;
    }

    private void setUpGameOutputFeed() {
        output = new TextArea();
        output.setPrefWidth(450);
        output.setMaxWidth(TextArea.USE_PREF_SIZE);
        output.setMinWidth(TextArea.USE_PREF_SIZE);
        output.setEditable(false);
        output.setPrefRowCount(10);
        output.setPrefColumnCount(20);
        output.setWrapText(true);
        output.setBackground(Background.EMPTY);
        output.getStylesheets().add("file:resources/text-area.css");
    }

    private Button getDisplayInstructionsButton() {
        Button displayInstructionsButton = new Button("Instruction");
        displayInstructionsButton.setOnAction(e -> {
            try {
                Desktop.getDesktop().browse(new URI("https://en.wikipedia.org/wiki/Draughts"));
                output.appendText("Instructions has been displayed in Your internet browser\n");
            } catch (Exception exception) {
                output.appendText("We're sorry. It seems that your browser can't be accessed at this time\n");
            }
        });

        displayInstructionsButton.setMaxWidth(Double.MAX_VALUE);
        return displayInstructionsButton;
    }

    private Button getNewGameButton() {
        Button newGameButton = new Button("Start new game");
        newGameButton.setOnAction(value -> game.restartGame(null));
        newGameButton.setMaxWidth(Double.MAX_VALUE);
        return newGameButton;
    }

    private Button getSaveGameButton() {
        Button saveGameButton = new Button("Save game");
        saveGameButton.setMaxWidth(Double.MAX_VALUE);
        return saveGameButton;
    }

    private Button getLoadGameButton() {
        Button loadGameButton = new Button("Load game");
        loadGameButton.setMaxWidth(Double.MAX_VALUE);
        return loadGameButton;
    }
}
