package com.mateoi.pong;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

/**
 * This class is the FX application that gets rendered on screen.
 *
 * @author mateo
 *
 */
public class PongFXApp extends Application {

    /** The game to play */
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

    /**
     * Checks if the given {@link KeyCode} is pressed.
     *
     * @param code
     * @return
     */
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

    /**
     * Adds keyboard listeners to the given scene.
     *
     * @param scene
     */
    private static void addSceneListeners(Scene scene) {
        scene.setOnKeyPressed(e -> {
            if (!input.contains(e.getCode())) {
                input.add(e.getCode());
            }
        });
        scene.setOnKeyReleased(e -> input.remove(e.getCode()));
    }

    /**
     * Set the state of the game
     *
     * @param game
     */
    public static void setGame(PongGame game) {
        PongFXApp.game = game;
    }

    /**
     * Set the players that will play the game
     *
     * @param leftPlayer
     * @param rightPlayer
     */
    public static void setPlayers(Player leftPlayer, Player rightPlayer) {
        PongFXApp.leftPlayer = leftPlayer;
        PongFXApp.rightPlayer = rightPlayer;
    }

    /**
     * Sets the graphical offset between the edge of the playing area and the
     * edge of the drawing canvas. Has no effect on gameplay, just graphics.
     *
     * @param wallOffset
     */
    public static void setWallOffset(int wallOffset) {
        PongFXApp.wallOffset = wallOffset;
    }

    /**
     * Set the thickness of the lines in the game. Has no effect on gameplay,
     * just graphics.
     * 
     * @param lineThickness
     */
    public static void setLineThickness(int lineThickness) {
        PongFXApp.lineThickness = lineThickness;
    }

}
