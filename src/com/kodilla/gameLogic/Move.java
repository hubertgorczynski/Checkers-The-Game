package com.kodilla.gameLogic;

import com.kodilla.alertMessages.InvalidMoveError;

public class Move {

    private final Coordinates origin;
    private final Coordinates target;
    private final MoveType type;
    private boolean kingCreated;

    private com.kodilla.alertMessages.InvalidMoveError InvalidMoveError = null;

    public Move(Coordinates origin, Coordinates target, MoveType type) {
        this.origin = origin;
        this.target = target;
        this.type = type;
    }

    public MoveType getType() {
        return type;
    }

    public boolean isKingCreated() {
        return kingCreated;
    }

    public void createKing() {
        kingCreated = true;
    }

    public Coordinates getOrigin() {
        return origin;
    }

    public Coordinates getTarget() {
        return target;
    }

    public InvalidMoveError getInvalidMoveError() {
        return InvalidMoveError;
    }

    public void setInvalidMoveExplanation(InvalidMoveError InvalidMoveError) {
        this.InvalidMoveError = InvalidMoveError;
    }
}
