import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile extends Rectangle {

    private Unit unit;

    private final boolean light;

    public Tile(Coordinates position) {
        setWidth(Game.TILE_SIZE);
        setHeight(Game.TILE_SIZE);

        light = (position.x + position.y) % 2 == 0;
        resetTileColor();

        relocate(position.x * Game.TILE_SIZE, position.y * Game.TILE_SIZE);
    }

    public void resetTileColor() {
        if (light) {
            setFill(Color.valueOf("white"));
        } else {
            setFill(Color.valueOf("black"));
        }
    }

    public boolean hasUnit() {
        return unit != null;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public void highlightAttackDestination() {
        setFill(Color.valueOf("red"));
    }

    public void highlightMoveDestination() {
        setFill(Color.valueOf("blue"));
    }

    public void highlightUnit() {
        setFill(Color.valueOf("green"));
    }

    public void highlightAIMove() {
        setFill(Color.valueOf("orange"));
    }
}