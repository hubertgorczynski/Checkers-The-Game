package checkers;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile extends Rectangle {

    private Pawn pawn;

    public boolean hasPawn() {
        return pawn != null;
    }

    public Pawn getPawn() {
        return pawn;
    }

    public void setPawn(Pawn pawn) {
        this.pawn = pawn;
    }

    public Tile(boolean light, int x, int y) {
        setWidth(Checkers.TILE_SIZE);
        setHeight(Checkers.TILE_SIZE);

        relocate(x * Checkers.TILE_SIZE, y * Checkers.TILE_SIZE);

        if (light) {
            setFill(Color.valueOf("#FFF8DC"));
        } else
            setFill(Color.valueOf("#708090"));
    }

}
