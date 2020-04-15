package com.kodilla.alertMessages;

public enum InvalidMoveError {

    OUTSIDE_BOARD_ERROR("\nYou can not move beyond borders of the game board.\n"),
    SAME_POSITION_ERROR("\nYou can not choose the same position tile.\n"),
    TILE_ALREADY_OCCUPIED_ERROR("\nThis tile is already occupied by another pawn.\n"),
    NOT_PLAY_SQUARE_ERROR("\nInvalid tiles color pick. You can move forward, diagonally only.\n"),
    DISTANT_MOVE_ERROR("\nInvalid distance move. You can only move forward, diagonally to the nearest tile.\n");
    private final String explanation;

    InvalidMoveError(String explanation) {
        this.explanation = explanation;
    }

    public String getExplanation() {
        return explanation;
    }
}