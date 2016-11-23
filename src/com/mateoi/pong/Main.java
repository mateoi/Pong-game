package com.mateoi.pong;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage window) {
        try {
            Group root = new Group();
            Scene scene = new Scene(root);
            window.setScene(scene);

            Canvas canvas = new Canvas(500, 300);
            root.getChildren().add(canvas);
            Game game = new Game(25, 0.02, 0.4, 0.2, 1.1, 1.8, canvas.getWidth(), canvas.getHeight());
            KeyboardInput in = new KeyboardInput(scene);
            Player leftPlayer = new HumanPlayer(in, KeyCode.W, KeyCode.S);
            Player rightPlayer = new HumanPlayer(in, KeyCode.UP, KeyCode.DOWN);
            GameController controller = new GameController(game, canvas, leftPlayer, rightPlayer);
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
