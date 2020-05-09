package com.kodilla.graphicContent;

import javafx.scene.control.Button;

public class MoveHighlightingManager {

    private boolean userMoveHighlighting = true;
    private boolean computerMoveHighlighting = true;
    Button userMoveHighlightingToggleButton;
    Button computerMoveHighlightingToggleButton;

    public MoveHighlightingManager(Button userMoveHighlightingToggleButton, Button computerMoveHighlightingToggleButton) {
        this.userMoveHighlightingToggleButton = userMoveHighlightingToggleButton;
        this.computerMoveHighlightingToggleButton = computerMoveHighlightingToggleButton;
    }

    public void toggleUserMovesHighlighting() {
        this.userMoveHighlighting = !this.userMoveHighlighting;

        if (this.userMoveHighlighting) {
            userMoveHighlightingToggleButton.setText("Disable: User moves highlighting \n");
        } else {
            userMoveHighlightingToggleButton.setText("Enable: User moves highlighting \n");
        }
    }

    public void toggleComputerMovesHighlighting() {
        this.computerMoveHighlighting = !this.computerMoveHighlighting;

        if (this.computerMoveHighlighting) {
            computerMoveHighlightingToggleButton.setText("Disable: Computer moves highlighting \n");
        } else {
            computerMoveHighlightingToggleButton.setText("Enable: Computer moves highlighting \n");
        }
    }

    public boolean isUserMoveHighlighting() {
        return userMoveHighlighting;
    }

    public boolean isComputerMoveHighlighting() {
        return computerMoveHighlighting;
    }
}
