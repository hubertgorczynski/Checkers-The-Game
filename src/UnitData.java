import java.io.Serializable;

// Class will be used in next update of a game - adding possibility to save game

public class UnitData implements Serializable {
    private final boolean isWhite;
    private final boolean isKing;

    public UnitData(boolean isWhite, boolean isKing) {
        this.isWhite = isWhite;
        this.isKing = isKing;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public boolean isKing() {
        return isKing;
    }
}

