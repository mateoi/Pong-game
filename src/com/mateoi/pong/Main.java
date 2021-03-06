package com.mateoi.pong;

import javafx.scene.input.KeyCode;

public class Main {

    public static void main(String[] args) {
        Player leftPlayer = new AIPlayer(true);
        Player rightPlayer = new HumanPlayer(KeyCode.UP, KeyCode.DOWN);
        PongGame game = new PongGame(500, 300);
        PongFXApp.setGame(game);
        PongFXApp.setPlayers(leftPlayer, rightPlayer);
        PongFXApp.launch(PongFXApp.class);

        System.out.println("Longest Rally: " + game.getLongestRally());
        System.out.println("Average Rally: " + game.getAverageRally());
        System.out.println("Dead balls: " + game.getDeadBalls());
    }
}
