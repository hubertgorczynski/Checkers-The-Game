package checkers;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

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

        // Make pawn (background and stroke - shadow)
        Ellipse backgroundColor = new Ellipse(TILE_SIZE * 0.3125, TILE_SIZE * 0.26);
        backgroundColor.setFill(Color.BLACK);

        backgroundColor.setStroke(Color.BLACK);
        backgroundColor.setStrokeWidth(TILE_SIZE * 0.03);

        backgroundColor.setTranslateX((TILE_SIZE - TILE_SIZE * 0.3125 * 2) / 2);
        backgroundColor.setTranslateY((TILE_SIZE - TILE_SIZE * 0.26 * 2) / 2 + TILE_SIZE * 0.07);

        Ellipse topColor = new Ellipse(TILE_SIZE * 0.3125, TILE_SIZE * 0.26);

        if (type == PawnType.GREY) {
            topColor.setFill(Color.valueOf("#808080"));
        } else if (type == PawnType.WHITE) {
            topColor.setFill(Color.valueOf("#F0FFF0"));
        }

        topColor.setStroke(Color.BLACK);
        topColor.setStrokeWidth(TILE_SIZE * 0.03);

        topColor.setTranslateX((TILE_SIZE - TILE_SIZE * 0.3125 * 2) / 2);
        topColor.setTranslateY((TILE_SIZE - TILE_SIZE * 0.26 * 2) / 2);

        getChildren().addAll(backgroundColor, topColor);

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


