package com.kodilla.gameLogic;

import com.kodilla.alertMessages.InvalidMoveError;
import com.kodilla.alertMessages.WinnerMessage;
import com.kodilla.graphicContent.Board;
import com.kodilla.graphicContent.GUI;
import com.kodilla.graphicContent.TextAreaManager;
import com.kodilla.saveLoadData.GameSaveData;
import com.kodilla.saveLoadData.UnitData;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Game {

    public static final int BOARD_SIZE = 8;
    public static final int TILE_SIZE = 100;
    public static final int AI_MOVE_LAG_TIME = 600;
    private boolean resetGame;
    public static boolean userMoveHighlighting = true;
    public static boolean aiMoveHighlighting = true;
    private Player blackPlayer;
    private Player whitePlayer;
    private Board board;
    private final Group components;
    private final TextAreaManager textAreaManager;

    public Game(Player blackPlayer, Player whitePlayer, TextAreaManager textAreaManager) {
        this.textAreaManager = textAreaManager;
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
        board = new Board(textAreaManager);
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

    public void toggleUserMoveHighlighting() {
        userMoveHighlighting = !userMoveHighlighting;
        refreshBoard();
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

    private void refreshBoard() {
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

    public void saveGame(Player player1, Player player2, Board board) {
        HashMap<Coordinates, UnitData> unitHashMap = new HashMap<>();
        for (Node node : components.getChildren()) {
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

        boolean player1IsHuman = player1.isPlayerHuman();
        boolean player2IsHuman = player2.isPlayerHuman();
        Team player1Team = player1.getPlayerTeam();
        Team player2Team = player2.getPlayerTeam();

        GameSaveData gameSaveData = new GameSaveData(
                unitHashMap,
                board.getCurrentTeam(),
                player1IsHuman,
                player2IsHuman,
                player1Team,
                player2Team,
                userMoveHighlighting,
                aiMoveHighlighting
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

    public void loadGame(Player player1, Player player2, Board board) {
        try {
            ObjectInputStream stream = new ObjectInputStream(new FileInputStream("save.txt"));
            GameSaveData loadData = (GameSaveData) stream.readObject();

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

            board.setCurrentTeam(loadData.getCurrentTeam());

            if (loadData.isPlayer1IsHuman()) {
                player1.setPlayerType(PlayerType.USER);
            } else {
                player1.setPlayerType(PlayerType.AI);
            }

            if (loadData.isPlayer2IsHuman()) {
                player2.setPlayerType(PlayerType.USER);
            } else {
                player2.setPlayerType(PlayerType.AI);
            }

            player1.setPlayerTeam(loadData.getPlayer1Team());
            player2.setPlayerTeam(loadData.getPlayer2Team());

            userMoveHighlighting = loadData.isUserMovesHighlighting();
            aiMoveHighlighting = loadData.isComputerMovesHighlighting();
            toggleUserMoveHighlightingTextButton(userMoveHighlighting);
            toggleComputerMoveHighlightingTextButton(aiMoveHighlighting);

        } catch (IOException e) {
            textAreaManager.display("\nSave file not found!");
        } catch (ClassNotFoundException e) {
            textAreaManager.display("\nSave file crashed!");
        }
    }

    private void toggleUserMoveHighlightingTextButton(boolean isHighlighting) {
        if (isHighlighting) {
            GUI.userMoveHighlightingToggleButton.setText("Disable: User moves highlighting \n");
        } else {
            GUI.userMoveHighlightingToggleButton.setText("Enable: User moves highlighting \n");
        }
    }

    private void toggleComputerMoveHighlightingTextButton(boolean isHighlighting) {
        if (isHighlighting) {
            GUI.AIMoveHighlightingToggleButton.setText("Disable: Computer moves highlighting \n");
        } else {
            GUI.AIMoveHighlightingToggleButton.setText("Enable: Computer moves highlighting \n");
        }
    }
}