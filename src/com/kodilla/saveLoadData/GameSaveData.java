package com.kodilla.saveLoadData;

import com.kodilla.gameLogic.Coordinates;
import com.kodilla.gameLogic.Team;

import java.io.Serializable;
import java.util.HashMap;

public class GameSaveData implements Serializable {
    private final HashMap<Coordinates, UnitData> mapData;
    private final Team currentTeamMove;
    boolean player1IsHuman;
    boolean player2IsHuman;
    Team player1Team;
    Team player2Team;
    private final boolean isHumanMovesHighlighting;
    private final boolean isComputerMovesHighlighting;

    public GameSaveData(HashMap<Coordinates, UnitData> mapData, Team currentTeamMove,
                        boolean player1IsHuman, boolean player2IsHuman, Team player1Team, Team player2Team,
                        boolean isHumanMovesHighlighting, boolean isComputerMovesHighlighting) {
        this.mapData = mapData;
        this.currentTeamMove = currentTeamMove;
        this.player1IsHuman = player1IsHuman;
        this.player2IsHuman = player2IsHuman;
        this.player1Team = player1Team;
        this.player2Team = player2Team;
        this.isHumanMovesHighlighting = isHumanMovesHighlighting;
        this.isComputerMovesHighlighting = isComputerMovesHighlighting;
    }

    public HashMap<Coordinates, UnitData> getMapData() {
        return mapData;
    }

    public Team getCurrentTeam() {
        return currentTeamMove;
    }

    public boolean isPlayer1IsHuman() {
        return player1IsHuman;
    }

    public boolean isPlayer2IsHuman() {
        return player2IsHuman;
    }

    public Team getPlayer1Team() {
        return player1Team;
    }

    public Team getPlayer2Team() {
        return player2Team;
    }

    public boolean isUserMovesHighlighting() {
        return isHumanMovesHighlighting;
    }

    public boolean isComputerMovesHighlighting() {
        return isComputerMovesHighlighting;
    }
}
