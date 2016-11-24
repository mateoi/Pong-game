package com.mateoi.pong;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class PongFXApp extends Application {

    private static PongGame game;

    // Graphics Parameters
    private static int wallOffset = 10;
    private static int lineThickness = 3;

    private static Player leftPlayer;
    private static Player rightPlayer;

    /** List to keep the currently pressed keys in */
    private static ArrayList<KeyCode> input = new ArrayList<>();

    public PongFXApp() {
        // Nothing here
    }

    public static boolean keyPressed(KeyCode code) {
        return input.contains(code);
    }

    @Override
    public void start(Stage window) throws Exception {
        try {
            Group root = new Group();
            Scene scene = new Scene(root);
            window.setScene(scene);
            addSceneListeners(scene);
            Canvas canvas = new Canvas(game.getFieldWidth() + 2 * wallOffset, game.getFieldHeight() + 2 * wallOffset);
            root.getChildren().add(canvas);
            PongController controller = new PongController(game, canvas, leftPlayer, rightPlayer, wallOffset,
                    lineThickness);
            window.show();
            controller.playGame();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addSceneListeners(Scene scene) {
        scene.setOnKeyPressed(e -> {
            if (!input.contains(e.getCode())) {
                input.add(e.getCode());
            }
        });
        scene.setOnKeyReleased(e -> input.remove(e.getCode()));

    }

    public static void setGame(PongGame game) {
        PongFXApp.game = game;
    }

    public static void setPlayers(Player leftPlayer, Player rightPlayer) {
        PongFXApp.leftPlayer = leftPlayer;
        PongFXApp.rightPlayer = rightPlayer;
    }

    public static void setWallOffset(int wallOffset) {
        PongFXApp.wallOffset = wallOffset;
    }

    public static void setLineThickness(int lineThickness) {
        PongFXApp.lineThickness = lineThickness;
    }

}
