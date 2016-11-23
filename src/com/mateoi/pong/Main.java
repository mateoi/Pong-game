package com.mateoi.pong;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class Main extends Application {

    // Game Parameters
    private static final int gameHeight = 300;
    private static final int gameWidth = 500;
    private static final double paddleSize = 25;
    private static final double paddleCurvature = 0.02;
    private static final double paddleAcceleration = 0.4;
    private static final double paddleFriction = 0.2;
    private static final double spinFactor = 0.05;
    private static final double elasticCoefficient = 1.1;
    private static final double initialSpeed = 1.8;

    // Graphical Parameters
    private static final int wallOffset = 10;
    private static final int lineThickness = 3;

    @Override
    public void start(Stage window) {
        try {
            Group root = new Group();
            Scene scene = new Scene(root);
            window.setScene(scene);

            Canvas canvas = new Canvas(gameWidth + 2 * wallOffset, gameHeight + 2 * wallOffset);
            root.getChildren().add(canvas);
            Game game = new Game(paddleSize, paddleCurvature, paddleAcceleration, paddleFriction, elasticCoefficient,
                    spinFactor, initialSpeed, gameWidth, gameHeight);
            KeyboardInput in = new KeyboardInput(scene);
            Player leftPlayer = new HumanPlayer(in, KeyCode.W, KeyCode.S);
            Player rightPlayer = new HumanPlayer(in, KeyCode.UP, KeyCode.DOWN);
            GameController controller = new GameController(game, canvas, leftPlayer, rightPlayer, wallOffset,
                    lineThickness);
            window.show();
            controller.playGame();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
