package com.mateoi.pong;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class PongFXApp extends Application {

    // Default Game Parameters
    private static int gameHeight = 300;
    private static int gameWidth = 500;
    private static double paddleSize = 25;
    private static double paddleCurvature = 0.02;
    private static double paddleAcceleration = 0.4;
    private static double paddleFriction = 0.2;
    private static double spinFactor = 0.05;
    private static double elasticCoefficient = 1.1;
    private static double initialSpeed = 1.8;

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
            Canvas canvas = new Canvas(gameWidth + 2 * wallOffset, gameHeight + 2 * wallOffset);
            root.getChildren().add(canvas);
            PongGame game = new PongGame(paddleSize, paddleCurvature, paddleAcceleration, paddleFriction,
                    elasticCoefficient, spinFactor, initialSpeed, gameWidth, gameHeight);
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

    public static void setGameHeight(int gameHeight) {
        PongFXApp.gameHeight = gameHeight;
    }

    public static void setGameWidth(int gameWidth) {
        PongFXApp.gameWidth = gameWidth;
    }

    public static void setPaddleSize(double paddleSize) {
        PongFXApp.paddleSize = paddleSize;
    }

    public static void setPaddleCurvature(double paddleCurvature) {
        PongFXApp.paddleCurvature = paddleCurvature;
    }

    public static void setPaddleAcceleration(double paddleAcceleration) {
        PongFXApp.paddleAcceleration = paddleAcceleration;
    }

    public static void setPaddleFriction(double paddleFriction) {
        PongFXApp.paddleFriction = paddleFriction;
    }

    public static void setSpinFactor(double spinFactor) {
        PongFXApp.spinFactor = spinFactor;
    }

    public static void setElasticCoefficient(double elasticCoefficient) {
        PongFXApp.elasticCoefficient = elasticCoefficient;
    }

    public static void setInitialSpeed(double initialSpeed) {
        PongFXApp.initialSpeed = initialSpeed;
    }

    public static void setWallOffset(int wallOffset) {
        PongFXApp.wallOffset = wallOffset;
    }

    public static void setLineThickness(int lineThickness) {
        PongFXApp.lineThickness = lineThickness;
    }

    public static void setPlayers(Player leftPlayer, Player rightPlayer) {
        PongFXApp.leftPlayer = leftPlayer;
        PongFXApp.rightPlayer = rightPlayer;
    }
}
