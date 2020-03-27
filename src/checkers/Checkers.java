package checkers;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Checkers extends Application {

    public static final int TILE_SIZE = 100;
    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;

    private Tile[][] board = new Tile[WIDTH][HEIGHT];

    private Group tileGroup = new Group();
    private Group pawnsGroup = new Group();

    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        root.getChildren().addAll(tileGroup, pawnsGroup);


        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Tile tile = new Tile((x + y) % 2 == 0, x, y);
                board[x][y] = tile;

                tileGroup.getChildren().add(tile);

                Pawn pawn = null;

                // Ustawia szare pionki
                if (y <= 2 && (x + y) % 2 != 0) {
                    pawn = makePawn(PawnType.GREY, x, y);
                }

                // Ustawia białe pionki
                if (y >= 5 && (x + y) % 2 != 0) {
                    pawn = makePawn(PawnType.WHITE, x, y);
                }

                if (pawn != null) {
                    tile.setPawn(pawn);
                    pawnsGroup.getChildren().add(pawn);
                }
            }
        }
        return root;
    }

    /*
    MoveResult sprawdza czy:
    1. jest już na planszy (tablicy) pionek przed nim oraz czy
    ruch jest po dwóch polach.
    2. czy wykonuje dwa ruchy i na drodze jest inny pionek - bicie;
    3. jeśli nie ma ani pionka ani ruchu o 2 pola to nic nie robi.
    */

    private MoveResult tryMove(Pawn pawn, int newX, int newY) {
        if (board[newX][newY].hasPawn() || (newX + newY) % 2 == 0) {
            return new MoveResult(MoveType.NONE);
        }

        int x0 = toBoard(pawn.getOldX());
        int y0 = toBoard(pawn.getOldY());

        if (Math.abs(newX - x0) == 1 && newY - y0 == pawn.getType().moveDirection) {
            return new MoveResult(MoveType.NORMAL);
        } else if ((Math.abs(newX - x0) == 2 && newY - y0 == pawn.getType().moveDirection * 2)) {
            int x1 = x0 + (newX - x0) / 2;
            int y1 = y0 + (newY - y0) / 2;

            if (board[x1][y1].hasPawn() && board[x1][y1].getPawn().getType() != pawn.getType()) {
                return new MoveResult(MoveType.KILL, board[x1][y1].getPawn());
            }
        }

        return new MoveResult((MoveType.NONE));
    }


    /*
    toBoard konwertuje współrzędne z współrzędnych (pixeli) do współrzędnych na tablicy
    */

    private int toBoard(double pixel) {
        return (int) (pixel + TILE_SIZE / 2) / TILE_SIZE;
    }

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(createContent());
        primaryStage.setTitle("Checkers");
        primaryStage.setScene(scene);
        primaryStage.show();
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

    public static void main(String[] args) {
        launch(args);
    }
}



