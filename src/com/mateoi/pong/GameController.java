package com.mateoi.pong;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import javafx.animation.AnimationTimer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Displays a Pong game.
 *
 * @author mateo
 */
public class GameController {

    /** The game that's being played */
    private Game game;
    /** The canvas to draw the game on */
    private Canvas canvas;
    /** Context used to draw on the canvas */
    private GraphicsContext gc;
    /** Player that controls the left paddle */
    private Player leftPlayer;
    /** Player that controls the right paddle */
    private Player rightPlayer;

    /** Whether the game is finished or not */
    private BooleanProperty done = new SimpleBooleanProperty(false);

    /** Score to win the game */
    private int winningScore = 10;
    /** How thick to draw the lines on screen */
    private int lineThickness = 3;

    /**
     * Creates a new controller that will play the game getting moves from both
     * players and draw it on the canvas
     *
     * @param game
     * @param canvas
     * @param leftPlayer
     * @param rightPlayer
     */
    public GameController(Game game, Canvas canvas, Player leftPlayer, Player rightPlayer) {
        this.game = game;
        this.canvas = canvas;
        this.leftPlayer = leftPlayer;
        this.rightPlayer = rightPlayer;
        gc = canvas.getGraphicsContext2D();
        gc.setFont(Font.font("monospace", FontWeight.BOLD, 15));
        done.addListener((a, b, done) -> {
            if (done) {
                String winner = game.getLeftScore() > game.getRightScore() ? "Left" : "Right";
                System.out.println(winner + " player wins!");
                loop.stop();
            }
        });
    }

    /**
     * The graphics loop that handles drawing each frame.
     */
    private AnimationTimer loop = new AnimationTimer() {

        @Override
        public void handle(long now) {
            drawField();
            int leftMove = leftPlayer.move(game);
            int rightMove = rightPlayer.move(game);
            game.nextFrame(leftMove, rightMove);
            done.set(game.getLeftScore() >= winningScore || game.getRightScore() >= winningScore);
        }

        /**
         * Draws the field: walls, paddles, ball and scores on a black
         * background.
         */
        private void drawField() {
            gc.setFill(Color.BLACK);
            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
            gc.setFill(Color.WHITE);
            drawPaddles(game.getLeftPaddleCenter(), game.getRightPaddleCenter(), game.getPaddleRadius(),
                    game.getFieldWidth());
            drawWalls(Game.wallOffset, game.getFieldWidth(), game.getFieldHeight());
            drawBall(game.getBallPosition());
            drawScores(game.getLeftScore(), game.getRightScore(), game.getFieldWidth() / 2);
        }

        /**
         * Puts the score on the top
         *
         * @param leftScore
         * @param rightScore
         * @param centerline
         */
        private void drawScores(int leftScore, int rightScore, double centerline) {
            gc.fillText(String.valueOf(leftScore), centerline - 20, 25.);
            gc.fillText(String.valueOf(rightScore), centerline + 5, 25.);
        }

        /**
         * Draws the ball in its correct position
         *
         * @param ballPosition
         */
        private void drawBall(Vector2D ballPosition) {
            double x = ballPosition.getX();
            double y = ballPosition.getY();
            gc.fillRect(x - lineThickness / 2, y - lineThickness / 2, lineThickness, lineThickness);
        }

        /**
         * Draws the walls at the given offset.
         *
         * @param walloffset
         * @param width
         * @param height
         */
        private void drawWalls(double walloffset, double width, double height) {
            gc.fillRect(0, walloffset - lineThickness, width, lineThickness);
            gc.fillRect(0, height - walloffset, width, lineThickness);
        }

        /**
         * Draws the paddles
         *
         * @param leftPaddleCenter
         * @param rightPaddleCenter
         * @param paddleRadius
         * @param width
         */
        private void drawPaddles(Vector2D leftPaddleCenter, Vector2D rightPaddleCenter, double paddleRadius,
                double width) {
            double lx = leftPaddleCenter.getX();
            double ly = leftPaddleCenter.getY();
            double rx = rightPaddleCenter.getX();
            double ry = rightPaddleCenter.getY();

            gc.fillRect(lx - lineThickness, ly - paddleRadius, lineThickness, 2 * paddleRadius);
            gc.fillRect(rx, ry - paddleRadius, lineThickness, 2 * paddleRadius);
        }
    };

    public void playGame() {
        loop.start();
    }
}
