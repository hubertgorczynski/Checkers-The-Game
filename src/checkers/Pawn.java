package checkers;

import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import static checkers.Checkers.TILE_SIZE;

public class Pawn extends StackPane {

    private PawnType type;

    private double mouseX, mouseY;
    private double oldX, oldY;

    public PawnType getType() {
        return type;
    }

    public double getOldX() {
        return oldX;
    }

    public double getOldY() {
        return oldY;
    }

    public Pawn(PawnType type, int x, int y) {
        this.type = type;

        Circle circle = new Circle(TILE_SIZE * 0.5);

        Image blackPawnGraphic = new Image("file:resources/black_pawn.png");
        Image whitePawnGraphic = new Image("file:resources/white_pawn.png");

        if (type == PawnType.GREY) {
            circle.setFill(new ImagePattern(blackPawnGraphic));
        } else if (type == PawnType.WHITE) {
            circle.setFill(new ImagePattern(whitePawnGraphic));
        }

        getChildren().add(circle);

        //Controlling by mouse
        move(x, y);

        setOnMousePressed(e -> {
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();
        });

        setOnMouseDragged(e ->
                relocate(e.getSceneX() - mouseX + oldX, e.getSceneY() - mouseY + oldY));
    }

    public void move(int x, int y) {
        oldX = x * TILE_SIZE;
        oldY = y * TILE_SIZE;
        relocate(oldX, oldY);
    }

    public void abortMove() {
        relocate(oldX, oldY);
    }
}


