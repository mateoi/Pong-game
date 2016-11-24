package com.mateoi.pong;

import javafx.scene.input.KeyCode;

public class Main {

    public static void main(String[] args) {
        Player leftPlayer = new HumanPlayer(KeyCode.W, KeyCode.S);
        Player rightPlayer = new HumanPlayer(KeyCode.UP, KeyCode.DOWN);
        PongFXApp.setPlayers(leftPlayer, rightPlayer);
        PongFXApp.launch(PongFXApp.class);
    }
}
