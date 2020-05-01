package com.kodilla.saveLoadData;

import com.kodilla.gameLogic.Coordinates;
import com.kodilla.gameLogic.Team;

import java.io.Serializable;
import java.util.HashMap;

// Class will be used in next update of a game - adding possibility to save game

public class GameSaveData implements Serializable {
    private final HashMap<Coordinates, UnitData> mapData;
    private final Team currentTeamMove;
    private final boolean humanPlaysWhiteUnits;
    private final boolean humanPlaysBlackUnits;
    private final boolean isHumanMovesHighlighting;
    private final boolean isComputerMovesHighlighting;

    public GameSaveData(HashMap<Coordinates, UnitData> mapData, Team currentTeam, boolean humanPlaysWhiteUnits,
                        boolean humanPlaysBlackUnits, boolean isHumanMovesHighlighting, boolean isComputerMovesHighlighting) {
        this.mapData = mapData;
        this.currentTeamMove = currentTeam;
        this.humanPlaysWhiteUnits = humanPlaysWhiteUnits;
        this.humanPlaysBlackUnits = humanPlaysBlackUnits;
        this.isHumanMovesHighlighting = isHumanMovesHighlighting;
        this.isComputerMovesHighlighting = isComputerMovesHighlighting;
    }

    public Team getCurrentTeam() {
        return currentTeamMove;
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
