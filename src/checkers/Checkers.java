package checkers;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Checkers extends Application {

    public static final int TILE_SIZE = 100;
    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;

    private Tile[][] board = new Tile[WIDTH][HEIGHT];

    private Group tileGroup = new Group();
    private Group pawnsGroup = new Group();

    private Parent createContent() {
        Pane pane = new Pane();
        pane.setPrefSize(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        pane.getChildren().addAll(tileGroup, pawnsGroup);

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Tile tile = new Tile((x + y) % 2 == 0, x, y);
                board[x][y] = tile;

                tileGroup.getChildren().add(tile);

                Pawn pawn = null;

                // Set grey pawns on the board (pane) - at start stage of the game
                if (y <= 2 && (x + y) % 2 != 0) {
                    pawn = makePawn(PawnType.GREY, x, y);
                }

                // Set white pawns on the board (pane) - at start stage of the game
                if (y >= 5 && (x + y) % 2 != 0) {
                    pawn = makePawn(PawnType.WHITE, x, y);
                }

                if (pawn != null) {
                    tile.setPawn(pawn);
                    pawnsGroup.getChildren().add(pawn);
                }
            }
        }
        return pane;
    }

    private Pawn makePawn(PawnType type, int x, int y) {
        Pawn pawn = new Pawn(type, x, y);

        pawn.setOnMouseReleased(e -> {
            int newX = toBoard(pawn.getLayoutX());
            int newY = toBoard(pawn.getLayoutY());

            MoveResult result = tryMove(pawn, newX, newY);

            int x0 = toBoard(pawn.getOldX());
            int y0 = toBoard(pawn.getOldY());

            switch (result.getType()) {
                case NONE:
                    pawn.abortMove();
                    break;
                case NORMAL:
                    pawn.move(newX, newY);
                    board[x0][y0].setPawn(null);
                    board[newX][newY].setPawn(pawn);
                    break;
                case KILL:
                    pawn.move(newX, newY);
                    board[x0][y0].setPawn(null);
                    board[newX][newY].setPawn(pawn);

                    Pawn otherPawn = result.getPawn();
                    board[toBoard(otherPawn.getOldX())][toBoard(otherPawn.getOldY())].setPawn(null);
                    pawnsGroup.getChildren().remove(otherPawn);
                    break;
            }
        });
        return pawn;
    }

    private MoveResult tryMove(Pawn pawn, int newX, int newY) {
        if (board[newX][newY].hasPawn() || (newX + newY) % 2 == 0) {
            return new MoveResult(MoveType.NONE);
        }

        int x0 = toBoard(pawn.getOldX());
        int y0 = toBoard(pawn.getOldY());

        if (Math.abs(newX - x0) == 1 && (newY - y0) == pawn.getType().moveDirection) {
            return new MoveResult(MoveType.NORMAL);
        } else if (Math.abs(newX - x0) == 2 && newY - y0 == pawn.getType().moveDirection * 2) {
            int x1 = x0 + (newX - x0) / 2;
            int y1 = y0 + (newY - y0) / 2;

            if (board[x1][y1].hasPawn() && board[x1][y1].getPawn().getType() != pawn.getType()) {
                return new MoveResult(MoveType.KILL, board[x1][y1].getPawn());
            }
        }

        return new MoveResult((MoveType.NONE));
    }

    // "toBoard" method converts coordinates from pixels board's coordinates
    private int toBoard(double pixel) {
        return (int) (pixel + TILE_SIZE / 2) / TILE_SIZE;
    }

    @Override
    public void start(Stage primaryStage) {

        Scene gameScene = new Scene(createContent());

        Image imageback = new Image("file:resources/main_menu.jpg");

        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(imageback, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);

        GridPane menuLayout = new GridPane();
        menuLayout.setHgap(8);
        menuLayout.setVgap(8);
        menuLayout.setBackground(background);

        Button startButton = new Button("New game");
        startButton.setPrefSize(180, 80);
        startButton.setStyle("-fx-font-size:20");
        startButton.setOnAction(event -> primaryStage.setScene(gameScene));

        Button loadButton = new Button("Load game");
        loadButton.setPrefSize(180, 80);
        loadButton.setStyle("-fx-font-size:20");

        Button exitButton = new Button("Exit");
        exitButton.setPrefSize(180, 80);
        exitButton.setStyle("-fx-font-size:20");

        menuLayout.add(startButton, 18, 15);
        menuLayout.add(loadButton, 18, 30);
        menuLayout.add(exitButton, 18, 45);

        Scene mainMenu = new Scene(menuLayout, 1500, 996);

        primaryStage.setScene(mainMenu);
        primaryStage.setTitle("Checkers");
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}



