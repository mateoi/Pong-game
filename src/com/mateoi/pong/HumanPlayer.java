package com.mateoi.pong;

import javafx.scene.input.KeyCode;

public class HumanPlayer implements Player {
    private final KeyboardInput input;
    private final KeyCode up;
    private final KeyCode down;

    public HumanPlayer(KeyboardInput input, KeyCode up, KeyCode down) {
        this.up = up;
        this.down = down;
        this.input = input;
    }

    @Override
    public int move(Game state) {
        int move = 0;
        if (input.contains(down)) {
            move++;
        }
        if (input.contains(up)) {
            move--;
        }
        return move;
    }

}
