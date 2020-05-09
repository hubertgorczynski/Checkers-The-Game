package com.kodilla.graphicContent;

import com.kodilla.gameLogic.*;
import javafx.scene.Group;
import javafx.scene.Node;

import java.util.ArrayList;

public class Board {

    private Team currentTeam;
    private final Group GUIComponents = new Group();
    private final Group tiles = new Group();
    private final Tile[][] board;
    private final Group blackUnits = new Group();
    private final Group whiteUnits = new Group();
    private ArrayList<Move> possibleMoves = new ArrayList<>();
    private final TextAreaManager textAreaManager;
    private final MoveHighlightingManager moveHighlightingManager;

    public Board(TextAreaManager textAreaManager, MoveHighlightingManager moveHighlightingManager) {
        this.textAreaManager = textAreaManager;
        this.moveHighlightingManager = moveHighlightingManager;
        board = new Tile[Game.BOARD_SIZE][Game.BOARD_SIZE];
        setCurrentTeam(Team.BLACK);

        generateBoard();
        populateBoard();

        GUIComponents.getChildren().setAll(tiles, blackUnits, whiteUnits);
    }

    public ArrayList<Move> prioritiseAttackMoves(ArrayList<Move> possibleMoves) {
        ArrayList<Move> attackMoves = new ArrayList<>();
        for (Move move : possibleMoves) {
            if (move.getType() == MoveType.KILL) {
                attackMoves.add(move);
            }
        }

        if (!attackMoves.isEmpty()) {
            return attackMoves;
        } else {
            return possibleMoves;
        }
    }

    public boolean isEnemyOnEdge(Coordinates unitPosition) {
        return Coordinates.isBoardEdge(unitPosition);
    }

    public void setNextPlayer() {
        if (currentTeam == Team.BLACK) {
            currentTeam = Team.WHITE;
        } else {
            currentTeam = Team.BLACK;
        }
    }

    public Team getCurrentTeam() {
        return currentTeam;
    }

    public void setCurrentTeam(Team team) {
        this.currentTeam = team;
    }

    private void generateBoard() {
        for (int y = 0; y < Game.BOARD_SIZE; y++) {
            for (int x = 0; x < Game.BOARD_SIZE; x++) {

                generateTile(new Coordinates(x, y));
            }
        }
    }

    private void generateTile(Coordinates position) {
        Tile tile = new Tile(position);
        board[position.x][position.y] = tile;
        tiles.getChildren().add(tile);
    }

    private void populateBoard() {
        populateBlack();
        populateWhite();
    }

    private void populateBlack() {
        int factionBorder = Math.round(Game.BOARD_SIZE / 3f);
        for (int y = 0; y < Game.BOARD_SIZE; y++) {
            for (int x = 0; x < Game.BOARD_SIZE; x++) {
                Coordinates position = new Coordinates(x, y);
                if (y < factionBorder && position.isPlaySquare()) {
                    spawnUnit(position, blackUnits, Team.BLACK);
                }
            }
        }
    }

    private void populateWhite() {
        int factionBorder = Math.round((Game.BOARD_SIZE / 3f) * 2);
        for (int y = Game.BOARD_SIZE - 1; y >= 0; y--) {
            for (int x = Game.BOARD_SIZE - 1; x >= 0; x--) {
                Coordinates position = new Coordinates(x, y);
                if (y >= factionBorder && position.isPlaySquare()) {
                    spawnUnit(position, whiteUnits, Team.WHITE);
                }
            }
        }
    }

    private void spawnUnit(Coordinates position, Group teamUnits, Team team) {
        Unit unit = new Unit(UnitType.PAWN, team, position);
        getTile(position).setUnit(unit);
        teamUnits.getChildren().add(unit);
    }

    public Group getGUIComponents() {
        return GUIComponents;
    }

    public void resetTileColors() {
        for (Node node : tiles.getChildren()) {
            Tile tile = (Tile) node;
            tile.resetTileColor();
        }
    }

    public void highlightUsersAvailableMoves() {
        if (moveHighlightingManager.isUserMoveHighlighting()) {
            for (Move move : possibleMoves) {
                highlightMove(move, PlayerType.USER);
            }
        }
    }

    public void highlightAIMove(Move move) {
        if (moveHighlightingManager.isComputerMoveHighlighting()) {
            highlightMove(move, PlayerType.AI);
        }
    }

    private void highlightMove(Move move, PlayerType playerType) {
        Coordinates destination = move.getTarget();
        Coordinates origin = move.getOrigin();
        switch (playerType) {
            case AI:
                getTile(destination).highlightAIMove();
                getTile(origin).highlightAIMove();
                break;
            case USER:
                if (move.getType() == MoveType.KILL) {
                    getTile(destination).highlightAttackDestination();
                } else {
                    getTile(destination).highlightMoveDestination();
                }
                getTile(origin).highlightUnit();
                break;
        }
    }

    public Tile getTile(Coordinates position) {
        return board[position.x][position.y];
    }

    public void refreshTeamsAvailableMoves(Team team) {
        ArrayList<Move> possibleTeamMoves = new ArrayList<>();
        Group teamUnits;

        if (team == Team.BLACK) {
            teamUnits = blackUnits;
        } else {
            teamUnits = whiteUnits;
        }

        for (Node node : teamUnits.getChildren()) {
            Unit unit = (Unit) node;
            possibleTeamMoves.addAll(getUnitsPossibleMoves(unit));
        }

        possibleMoves = prioritiseAttackMoves(possibleTeamMoves);
    }

    private ArrayList<Move> getUnitMoves(Unit unit) {
        return prioritiseAttackMoves(getUnitsPossibleMoves(unit));
    }

    private ArrayList<Move> getUnitsPossibleMoves(Unit unit) {
        ArrayList<Move> moves = new ArrayList<>();

        for (Coordinates adjacentPositions : unit.getAdjacentPositions()) {
            if (!isOccupiedTile(adjacentPositions)) {
                Move normalMove = new Move(unit.getPos(), adjacentPositions, MoveType.NORMAL);
                if (adjacentPositions.isEnemyKingRow(unit.getTeam()) && !unit.isKing()) {
                    normalMove.createKing();
                }
                moves.add(normalMove);
            } else if (isAttackPossible(adjacentPositions)) {
                Unit attackedUnit = getTile(adjacentPositions).getUnit();
                Move attackMove = new Move(unit.getPos(), adjacentPositions.getNextOnPath(), MoveType.KILL);
                if (adjacentPositions.getNextOnPath().isEnemyKingRow(unit.getTeam()) && !unit.isKing() ||
                        attackedUnit.isKing() && !unit.isKing()) {
                    attackMove.createKing();
                }
                moves.add(attackMove);
            }
        }
        return moves;
    }

    private boolean isAttackPossible(Coordinates adjacentTile) {
        return isEnemyUnit(adjacentTile) && !isEnemyOnEdge(adjacentTile) && !isOccupiedTile(adjacentTile.getNextOnPath());
    }

    public boolean isOccupiedTile(Coordinates position) {
        return getTile(position).hasUnit();
    }

    private boolean isEnemyUnit(Coordinates position) {
        Unit unit = getTile(position).getUnit();

        if (getCurrentTeam() == Team.BLACK) {
            return unit.isWhite();
        } else {
            return unit.isBlack();
        }
    }

    private void killUnit(Unit unit) {
        getTile(unit.getPos()).setUnit(null);
        if (unit.isBlack()) {
            blackUnits.getChildren().remove(unit);
        } else {
            whiteUnits.getChildren().remove(unit);
        }
    }

    public boolean executeMove(Move move) {
        Coordinates origin = move.getTarget().origin;
        Coordinates target = move.getTarget();
        Unit unit = getUnitAtPos(move.getOrigin());
        boolean kingIsCreated = move.isKingCreated();

        boolean turnFinished = false;
        switch (move.getType()) {
            case NONE:
                unit.abortMove();
                textAreaManager.display("Invalid move detected!");
                textAreaManager.display(move.getInvalidMoveError().getExplanation());
                break;
            case NORMAL:
                moveUnit(origin, target, unit, kingIsCreated);
                turnFinished = true;
                textAreaManager.display(unit.getTeam() + " made a normal move.\n");
                break;
            case KILL:
                Unit attackedUnit = getUnitAtPos(Coordinates.getKillCoords(move));
                moveUnit(origin, target, unit, kingIsCreated);
                killUnit(attackedUnit);

                if (canMove(unit) && canAttack(unit) && !move.isKingCreated()) {
                    possibleMoves = getUnitMoves(unit);
                } else {
                    turnFinished = true;
                }
                textAreaManager.display(unit.getTeam() + " killed enemy pawn.\n");
                break;
        }
        return turnFinished;
    }

    private boolean canAttack(Unit unit) {
        return getUnitMoves(unit).get(0).getType() == MoveType.KILL;
    }

    private boolean canMove(Unit unit) {
        return !getUnitMoves(unit).isEmpty();
    }

    public void moveUnit(Coordinates origin, Coordinates target, Unit unit, boolean kingIsCreated) {
        unit.move(target);
        getTile(origin).setUnit(null);
        getTile(target).setUnit(unit);
        if (kingIsCreated) {
            unit.toggleKing();
            textAreaManager.display("Pawn became a king!\n");
        }
    }

    public void makeCurrentTeamAccessible(Player blackPlayer, Player whitePlayer) {
        blackUnits.setMouseTransparent(!blackPlayer.isPlayersTurn());
        whiteUnits.setMouseTransparent(!whitePlayer.isPlayersTurn());
    }

    public Group getBlackUnits() {
        return blackUnits;
    }

    public Group getWhiteUnits() {
        return whiteUnits;
    }

    public ArrayList<Move> getPossibleMoves() {
        return possibleMoves;
    }

    private Unit getUnitAtPos(Coordinates position) {
        return getTile(position).getUnit();
    }
}
