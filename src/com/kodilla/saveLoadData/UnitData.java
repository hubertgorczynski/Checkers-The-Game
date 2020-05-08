package com.kodilla.saveLoadData;

import java.io.Serializable;

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

