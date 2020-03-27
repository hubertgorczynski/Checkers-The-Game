package checkers;

public enum PawnType {
    GREY(1), WHITE(-1);

    final int moveDirection;

    PawnType(int moveDirection) {
        this.moveDirection = moveDirection;
    }
}
