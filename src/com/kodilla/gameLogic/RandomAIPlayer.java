package com.kodilla.gameLogic;

import com.kodilla.graphicContent.Board;
import com.kodilla.graphicContent.GUI;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

public class RandomAIPlayer extends Player {

    private final Random rand = new Random();

    public RandomAIPlayer(Team playerTeam) {
        setPlayerTeam(playerTeam);
        resetPlayer();
        setPlayerType(PlayerType.AI);
    }

    @Override
    public Optional<Move> getPlayerMove(Board board) {
        GUI.output.appendText("Computer is thinking... \n");

        ArrayList<Move> possibleMoves = board.getPossibleMoves();
        int r = rand.nextInt(possibleMoves.size());
        return Optional.of(possibleMoves.get(r));
    }
}
