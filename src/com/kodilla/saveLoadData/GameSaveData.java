package com.kodilla.saveLoadData;

import com.kodilla.gameLogic.Coordinates;
import com.kodilla.gameLogic.Team;

import java.io.Serializable;
import java.util.HashMap;

public class GameSaveData implements Serializable {
    private final HashMap<Coordinates, UnitData> mapData;
    private final Team currentTeamMove;
    boolean blackPlayerIsHuman;
    boolean whitePlayerIsHuman;
    boolean blackPlayersTurn;
    boolean whitePlayersTurn;
    Team blackPlayerTeam;
    Team whitePlayerTeam;
    private final boolean userMoveHighlighting;
    private final boolean computerMoveHighlighting;
    private final String textAreaContent;

    public GameSaveData(HashMap<Coordinates, UnitData> mapData, Team currentTeamMove, boolean blackPlayerIsHuman,
                        boolean whitePlayerIsHuman, boolean blackPlayersTurn, boolean whitePlayersTurn,
                        Team blackPlayerTeam, Team whitePlayerTeam, boolean userMoveHighlighting,
                        boolean computerMoveHighlighting, String textAreaContent) {
        this.mapData = mapData;
        this.currentTeamMove = currentTeamMove;
        this.blackPlayerIsHuman = blackPlayerIsHuman;
        this.whitePlayerIsHuman = whitePlayerIsHuman;
        this.blackPlayersTurn = blackPlayersTurn;
        this.whitePlayersTurn = whitePlayersTurn;
        this.blackPlayerTeam = blackPlayerTeam;
        this.whitePlayerTeam = whitePlayerTeam;
        this.userMoveHighlighting = userMoveHighlighting;
        this.computerMoveHighlighting = computerMoveHighlighting;
        this.textAreaContent = textAreaContent;
    }

    public HashMap<Coordinates, UnitData> getMapData() {
        return mapData;
    }

    public Team getCurrentTeamMove() {
        return currentTeamMove;
    }

    public boolean isBlackPlayerIsHuman() {
        return blackPlayerIsHuman;
    }

    public boolean isWhitePlayerIsHuman() {
        return whitePlayerIsHuman;
    }

    public boolean isBlackPlayersTurn() {
        return blackPlayersTurn;
    }

    public boolean isWhitePlayersTurn() {
        return whitePlayersTurn;
    }

    public Team getBlackPlayerTeam() {
        return blackPlayerTeam;
    }

    public Team getWhitePlayerTeam() {
        return whitePlayerTeam;
    }

    public boolean isUserMoveHighlighting() {
        return userMoveHighlighting;
    }

    public boolean isComputerMoveHighlighting() {
        return computerMoveHighlighting;
    }

    public String getTextAreaContent() {
        return textAreaContent;
    }
}
