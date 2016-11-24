package com.mateoi.pong;

import javafx.scene.input.KeyCode;

/**
 * A Player that is controlled by keyboard input.
 *
 * @author mateo
 */
public class HumanPlayer implements Player {
    /** Key to use to move the paddle up */
    private final KeyCode up;
    /** Key to use to move the paddle down */
    private final KeyCode down;

    public HumanPlayer(KeyCode up, KeyCode down) {
        this.up = up;
        this.down = down;
    }

    /**
     * Moves the paddle according to the keys that are currently pressed. Does
     * not use the game state at all as this is a human input, so the state can
     * be anything.
     */
    @Override
    public int move(PongGame state) {
        int move = 0;
        if (PongFXApp.keyPressed(down)) {
            move++;
        }
        if (PongFXApp.keyPressed(up)) {
            move--;
        }
        return move;
    }

}
