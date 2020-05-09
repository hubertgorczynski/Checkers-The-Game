package com.kodilla.saveLoadData;

import com.kodilla.gameLogic.Coordinates;
import com.kodilla.gameLogic.Team;
import com.kodilla.graphicContent.MoveHighlightingManager;

import java.io.Serializable;
import java.util.HashMap;

public class GameSaveData implements Serializable {
    private final HashMap<Coordinates, UnitData> mapData;
    private final Team currentTeamMove;
    boolean blackPlayerIsHuman;
    boolean whitePlayerIsHuman;
    Team blackPlayerTeam;
    Team whitePlayerTeam;
    private final MoveHighlightingManager moveHighlightingManager;
    private final String textAreaContent;

    public GameSaveData(HashMap<Coordinates, UnitData> mapData, Team currentTeamMove,
                        boolean blackPlayerIsHuman, boolean whitePlayerIsHuman, Team blackPlayerTeam, Team whitePlayerTeam,
                        MoveHighlightingManager moveHighlightingManager, String textAreaContent) {
        this.mapData = mapData;
        this.currentTeamMove = currentTeamMove;
        this.blackPlayerIsHuman = blackPlayerIsHuman;
        this.whitePlayerIsHuman = whitePlayerIsHuman;
        this.blackPlayerTeam = blackPlayerTeam;
        this.whitePlayerTeam = whitePlayerTeam;
        this.moveHighlightingManager = moveHighlightingManager;
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

    public Team getBlackPlayerTeam() {
        return blackPlayerTeam;
    }

    public Team getWhitePlayerTeam() {
        return whitePlayerTeam;
    }

    public MoveHighlightingManager getMoveHighlightingManager() {
        return moveHighlightingManager;
    }

    public String getTextAreaContent() {
        return textAreaContent;
    }
}
