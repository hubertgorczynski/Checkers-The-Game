import java.io.Serializable;
import java.util.HashMap;

public class GameSaveData implements Serializable {
    private final HashMap<Coordinates, UnitData> mapData;
    private final boolean isWhiteTurn;
    private final boolean humanPlaysWhiteUnits;
    private final boolean humanPlaysBlackUnits;
    private final boolean isHumanMovesHighlighting;
    private final boolean isComputerMovesHighlighting;

    public GameSaveData(HashMap<Coordinates, UnitData> mapData, boolean isWhiteTurn, boolean humanPlaysWhiteUnits,
                        boolean humanPlaysBlackUnits, boolean isHumanMovesHighlighting, boolean isComputerMovesHighlighting) {
        this.mapData = mapData;
        this.isWhiteTurn = isWhiteTurn;
        this.humanPlaysWhiteUnits = humanPlaysWhiteUnits;
        this.humanPlaysBlackUnits = humanPlaysBlackUnits;
        this.isHumanMovesHighlighting = isHumanMovesHighlighting;
        this.isComputerMovesHighlighting = isComputerMovesHighlighting;
    }

    public boolean isWhiteTurn() {
        return isWhiteTurn;
    }

    public boolean isHumanPlaysWhiteUnits() {
        return humanPlaysWhiteUnits;
    }

    public HashMap<Coordinates, UnitData> getMapData() {
        return mapData;
    }

    public boolean isHumanPlaysBlackUnits() {
        return humanPlaysBlackUnits;
    }

    public boolean isHumanMovesHighlighting() {
        return isHumanMovesHighlighting;
    }

    public boolean isComputerMovesHighlighting() {
        return isComputerMovesHighlighting;
    }
}
