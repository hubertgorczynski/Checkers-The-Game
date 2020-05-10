package com.kodilla.gameLogic;

import com.kodilla.alertMessages.InvalidMoveError;
import com.kodilla.alertMessages.WinnerMessage;
import com.kodilla.graphicContent.Board;
import com.kodilla.graphicContent.GUI;
import com.kodilla.graphicContent.MoveHighlightingManager;
import com.kodilla.graphicContent.TextAreaManager;
import com.kodilla.saveLoadData.GameSaveData;
import com.kodilla.saveLoadData.UnitData;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Group;
import javafx.scene.Node;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {

    public static final int BOARD_SIZE = 8;
    public static final int TILE_SIZE = 100;
    public static final int AI_MOVE_LAG_TIME = 600;
    private boolean resetGame;
    private Player blackPlayer;
    private Player whitePlayer;
    private Board board;
    private final Group components;
    private final TextAreaManager textAreaManager;
    private final MoveHighlightingManager moveHighlightingManager;

    public Game(Player blackPlayer, Player whitePlayer, TextAreaManager textAreaManager, MoveHighlightingManager moveHighlightingManager) {
        this.textAreaManager = textAreaManager;
        this.moveHighlightingManager = moveHighlightingManager;
        components = new Group();
        this.blackPlayer = blackPlayer;
        this.whitePlayer = whitePlayer;
        resetGame();
        Platform.runLater(this::startNewGame);
    }

    private void startNewGame() {
        resetGame();
        textAreaManager.display(GUI.GAME_PREAMBLE_AND_INSTRUCTIONS);
        textAreaManager.display("\t\t\t\t--- New game begins! ---\n");
        printNewTurnDialogue();
        resetGame = false;
        nextPlayersTurn();
    }

    private void resetGame() {
        board = new Board(textAreaManager, moveHighlightingManager);
        setAllUnitsLocked();
        addMouseControlToAllUnits();
        components.getChildren().setAll(board.getGUIComponents().getChildren());

        blackPlayer.resetPlayer();
        whitePlayer.resetPlayer();
    }

    public void restartGame(Player player) {
        if (getCurrentPlayer().isPlayerHuman()) {
            setPlayer(player);
            startNewGame();
        } else {
            setPlayer(player);
            resetGame = true;
        }
    }

    private void setAllUnitsLocked() {
        board.getBlackUnits().setMouseTransparent(true);
        board.getWhiteUnits().setMouseTransparent(true);
    }

    private void nextPlayersTurn() {
        board.refreshTeamsAvailableMoves(getCurrentPlayer().getPlayerTeam());
        runNextMove();
    }

    private void runNextMove() {
        refreshBoard();
        if (isGameOver()) {
            Platform.runLater(() -> temporaryPause(500));
            Platform.runLater(this::startNewGame);
        } else {
            processPlayerMove(getCurrentPlayer());
        }
    }

    private void temporaryPause(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void refreshBoard() {
        board.resetTileColors();
        if (getCurrentPlayer().isPlayerHuman()) {
            Platform.runLater(() -> {
                board.highlightUsersAvailableMoves();
                board.makeCurrentTeamAccessible(blackPlayer, whitePlayer);
            });
        } else {
            setAllUnitsLocked();
        }
    }

    private Player getCurrentPlayer() {
        if (blackPlayer.isPlayersTurn()) {
            return blackPlayer;
        } else return whitePlayer;
    }

    private boolean isGameOver() {
        WinnerMessage winnerMessage = new WinnerMessage();
        boolean playAgain;
        if (board.getPossibleMoves().isEmpty()) {
            if (blackPlayer.isPlayersTurn()) {
                playAgain = winnerMessage.display("White");
            } else {
                playAgain = winnerMessage.display("Black");
            }
            if (playAgain) {
                return true;
            } else {
                System.exit(0);
            }
        }
        return resetGame;
    }

    private void processPlayerMove(Player player) {
        Task<Void> task = new Task<Void>() {
            @Override
            public Void call() {
                temporaryPause(AI_MOVE_LAG_TIME);

                player.getPlayerMove(board).ifPresent(move -> {
                    board.highlightAIMove(move);
                    temporaryPause(AI_MOVE_LAG_TIME);
                    Platform.runLater(() -> executePlayerMove(move));
                });
                return null;
            }
        };
        new Thread(task).start();
    }

    private void executePlayerMove(Move move) {
        boolean turnFinished = board.executeMove(move);
        if (turnFinished) {
            endTurn();
        } else {
            runNextMove();
        }
    }

    private void endTurn() {
        switchPlayerTurn();
        printNewTurnDialogue();
        nextPlayersTurn();
    }

    private void switchPlayerTurn() {
        blackPlayer.switchTurn();
        whitePlayer.switchTurn();
        board.setNextPlayer();
    }

    private void printNewTurnDialogue() {
        String player;
        if (blackPlayer.isPlayersTurn()) {
            player = "Black";
        } else {
            player = "White";
        }
        textAreaManager.display("------------------------------------------------------------------\n");
        textAreaManager.display(" \t \t \t \t \t " + player + "'s turn\n");
    }

    public Group getComponents() {
        return components;
    }

    private void setPlayer(Player player) {
        if (player != null) {
            if (player.getPlayerTeam() == Team.BLACK) {
                blackPlayer = player;
            } else {
                whitePlayer = player;
            }
        }
    }

    private void addMouseControlToAllUnits() {
        for (Node node : board.getBlackUnits().getChildren()) {
            Unit unit = (Unit) node;
            addMouseControlToUnit(unit);
        }
        for (Node node : board.getWhiteUnits().getChildren()) {
            Unit unit = (Unit) node;
            addMouseControlToUnit(unit);
        }
    }

    private void addMouseControlToUnit(Unit unit) {
        unit.setOnMouseReleased(e -> {
            int targetX = Coordinates.toBoard(unit.getLayoutX());
            int targetY = Coordinates.toBoard(unit.getLayoutY());

            Coordinates origin = unit.getPos();
            Coordinates mouseDragTarget = new Coordinates(origin, targetX, targetY);

            programUnitNormalMode(unit, origin, mouseDragTarget);

        });
    }

    private void programUnitNormalMode(Unit unit, Coordinates origin, Coordinates mouseDragTarget) {
        Move actualMove = null;
        for (Move move : board.getPossibleMoves()) {
            if (move.getOrigin().equals(origin) && move.getTarget().equals(mouseDragTarget)) {
                actualMove = move;
                break;
            }
        }
        if (actualMove == null) {
            actualMove = new Move(unit.getPos(), mouseDragTarget, MoveType.NONE);
            actualMove.setInvalidMoveExplanation(getInvalidMoveError(actualMove));
        }
        executePlayerMove(actualMove);
    }

    private InvalidMoveError getInvalidMoveError(Move move) {
        Coordinates mouseDragTarget = move.getTarget();
        Coordinates origin = move.getOrigin();

        InvalidMoveError invalidMoveError;
        if (mouseDragTarget.isOutsideBoard()) {
            invalidMoveError = InvalidMoveError.OUTSIDE_BOARD_ERROR;
        } else if (origin.equals(mouseDragTarget)) {
            invalidMoveError = InvalidMoveError.SAME_POSITION_ERROR;
        } else if (board.isOccupiedTile(mouseDragTarget)) {
            invalidMoveError = InvalidMoveError.TILE_ALREADY_OCCUPIED_ERROR;
        } else if (!mouseDragTarget.isPlaySquare()) {
            invalidMoveError = InvalidMoveError.NOT_PLAY_SQUARE_ERROR;
        } else {
            invalidMoveError = InvalidMoveError.DISTANT_MOVE_ERROR;
        }
        return invalidMoveError;
    }

    public void saveGame() {
        List<Node> allUnits = board.getBlackUnits().getChildren();
        allUnits.addAll(board.getWhiteUnits().getChildren());

        HashMap<Coordinates, UnitData> unitHashMap = new HashMap<>();
        for (Node node : allUnits) {
            if (node instanceof Unit) {
                Unit unit = (Unit) node;
                UnitData unitData = new UnitData(
                        unit.isWhite(),
                        unit.isKing()
                );
                Coordinates unitCoordinates = unit.getPos();
                unitHashMap.put(unitCoordinates, unitData);
            }
        }

        GameSaveData gameSaveData = new GameSaveData(
                unitHashMap,
                board.getCurrentTeam(),
                blackPlayer.isPlayerHuman(),
                whitePlayer.isPlayerHuman(),
                blackPlayer.getPlayerTeam(),
                whitePlayer.getPlayerTeam(),
                moveHighlightingManager.isUserMoveHighlighting(),
                moveHighlightingManager.isComputerMoveHighlighting(),
                textAreaManager.getTextAreaContent()
        );
        try {
            ObjectOutputStream saveData = new ObjectOutputStream(new FileOutputStream("save.txt"));
            saveData.writeObject(gameSaveData);
            saveData.close();
            textAreaManager.display("\nGame saved correctly!");
        } catch (IOException ex) {
            textAreaManager.display("\nCannot create save file!");
        }
    }

    public void loadGame() {
        try {
            ObjectInputStream stream = new ObjectInputStream(new FileInputStream("save.txt"));
            GameSaveData loadData = (GameSaveData) stream.readObject();
            stream.close();

            board.getWhiteUnits().getChildren().clear();
            board.getBlackUnits().getChildren().clear();

            HashMap<Coordinates, UnitData> unitDataHashMap = loadData.getMapData();
            for (Map.Entry<Coordinates, UnitData> unitDataEntry : unitDataHashMap.entrySet()) {
                Coordinates position = unitDataEntry.getKey();
                UnitType type;
                Team team;

                if (unitDataEntry.getValue().isKing()) {
                    type = UnitType.KING;
                } else {
                    type = UnitType.PAWN;
                }

                if (unitDataEntry.getValue().isWhite()) {
                    team = Team.WHITE;
                } else {
                    team = Team.BLACK;
                }

                Unit loadedUnit = new Unit(type, team, position);
                board.getTile(position).setUnit(loadedUnit);
                if (team == Team.WHITE) {
                    board.getWhiteUnits().getChildren().add(loadedUnit);
                } else {
                    board.getBlackUnits().getChildren().add(loadedUnit);
                }
            }

            board.setCurrentTeam(loadData.getCurrentTeamMove());

            if (loadData.isBlackPlayerIsHuman()) {
                blackPlayer.setPlayerType(PlayerType.USER);
            } else {
                blackPlayer.setPlayerType(PlayerType.AI);
            }

            if (loadData.isWhitePlayerIsHuman()) {
                whitePlayer.setPlayerType(PlayerType.USER);
            } else {
                blackPlayer.setPlayerType(PlayerType.AI);
            }

            blackPlayer.setPlayerTeam(loadData.getBlackPlayerTeam());
            whitePlayer.setPlayerTeam(loadData.getWhitePlayerTeam());

            moveHighlightingManager.setUserMoveHighlighting(loadData.isUserMoveHighlighting());
            moveHighlightingManager.setComputerMoveHighlighting(loadData.isComputerMoveHighlighting());

            textAreaManager.setTextAreaContent(loadData.getTextAreaContent());
            textAreaManager.display("\nGame loaded correctly!");
        } catch (IOException e) {
            textAreaManager.display("\nSave file not found!");
        } catch (ClassNotFoundException e) {
            textAreaManager.display("\nSave file crashed!");
        }
    }
}