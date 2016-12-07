package com.mateoi.pong;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * This class is represents the state and rules for a 2-player Pong game.
 *
 * @author mateo
 */
public class PongGame {

    /** Current position of the ball */
    private Vector2D ballPosition;
    /** Current velocity of the ball */
    private Vector2D ballVelocity;
    /** Initial ball velocity */
    private double initialBallSpeed = 1.8;
    /** Location of the left paddle's center */
    private Vector2D leftPaddleCenter;
    /** Current velocity of the left paddle */
    private Vector2D leftPaddleVelocity;
    /** Location of the right paddle's center */
    private Vector2D rightPaddleCenter;
    /** Current velocity of the right paddle */
    private Vector2D rightPaddleVelocity;

    /** Left player's score */
    private final IntegerProperty leftScore = new SimpleIntegerProperty(0);
    /** Right player's score */
    private final IntegerProperty rightScore = new SimpleIntegerProperty(0);

    /** How far the paddle extends from the center */
    private double paddleRadius = 25;
    /**
     * How much the paddle curves - this matters for bouncing and accelerating
     * the ball
     */
    private double paddleCurvature = 0.02;
    /** How fast the paddle accelerates when under user input */
    private double paddleAcceleration = 0.4;
    /** How quickly the paddle slows down when there is no user input */
    private double paddleFriction = 0.2;
    /** The increase in velocity when the ball bounces off the paddle */
    private double paddleElasticCoefficient = 1.1;
    /** How much the paddle's movement affects the ball's trajectory */
    private double spinFactor = 0.02;

    /** Total width of the field */
    private double fieldWidth;
    /** Total height of the field */
    private double fieldHeight;

    /** The maximum number of wall bounces before a re-serve */
    private final int maxWallBounces = 15;
    /** The current number of consecutive wall bounces */
    private int wallBounces = 0;

    // Game Statistics
    /** Longest rally in hits */
    private int longestRally = 0;
    /** Total paddle hits */
    private int totalPaddleHits = 0;
    /** Number of dead balls */
    private int deadBalls = 0;
    /** Length of current rally */
    private int currentRally = 0;
    /** Balls answered by the left player */
    private int leftHits = 0;
    /** Balls answered by the right player */
    private int rightHits = 0;

    /**
     * Creates a new Pong game.
     *
     * @param paddleRadius
     *            The size of the paddle from the center to the edge
     * @param paddleCurvature
     *            How much the ball curves depending on where on the paddle the
     *            ball hits
     * @param paddleAcceleration
     *            How quickly the paddle responds to input
     * @param paddleFriction
     *            How quickly the paddle stops moving once there is no more
     *            input
     * @param paddleElasticCoefficient
     *            How much the ball bounces off the paddle. Values over 1 mean
     *            the ball goes faster each bounce.
     * @param spinFactor
     *            How much the paddle's velocity affects the direction of the
     *            ball.
     * @param initialBallVelocity
     *            The initial speed, in pixels per frame, of the ball when
     *            served.
     * @param width
     *            The total width of the playing field
     * @param height
     *            The total height of the playing field.
     */
    public PongGame(double width, double height) {
        leftPaddleCenter = new Vector2D(0, height / 2);
        leftPaddleVelocity = Vector2D.ZERO;
        rightPaddleCenter = new Vector2D(width, height / 2);
        rightPaddleVelocity = Vector2D.ZERO;

        fieldWidth = width;
        fieldHeight = height;

        serve();
    }

    /**
     * Places the ball in the middle of the field with a random direction.
     */
    private void serve() {
        ballPosition = new Vector2D(fieldWidth / 2, fieldHeight / 2);
        ballVelocity = (new Vector2D(10 * Math.random() - 5, 2 * Math.random() - 1)).normalize()
                .scalarMultiply(initialBallSpeed);
        currentRally = 0;
    }

    /**
     * Updates the game state by one frame given the players' inputs
     *
     * @param leftMove
     * @param rightMove
     */
    public void nextFrame(int leftMove, int rightMove) {
        if (wallBounces >= maxWallBounces) {
            deadBalls++;
            wallBounces = 0;
            serve();
        }
        leftPaddleVelocity = acceleratePaddle(leftPaddleVelocity, leftPaddleCenter, leftMove);
        rightPaddleVelocity = acceleratePaddle(rightPaddleVelocity, rightPaddleCenter, rightMove);
        leftPaddleCenter = movePaddle(leftPaddleCenter, leftPaddleVelocity);
        rightPaddleCenter = movePaddle(rightPaddleCenter, rightPaddleVelocity);
        moveBall();
        checkScore();
    }

    /**
     * Changes a paddle's velocity given the user's move, and what paddle is
     * meant to move.
     *
     * @param isRightPaddle
     * @param move
     */
    private Vector2D acceleratePaddle(Vector2D velocity, Vector2D position, int move) {
        final boolean atBorder = position.getY() <= paddleRadius && move == -1
                || position.getY() >= fieldHeight - paddleRadius && move == 1;
        if (move == 0 || atBorder) {
            return velocity.scalarMultiply(1 - paddleFriction);
        } else {
            final Vector2D deltaV = new Vector2D(0, move * paddleAcceleration);
            return velocity.add(deltaV);
        }
    }

    /**
     * Moves paddles and clips their location.
     */
    private Vector2D movePaddle(Vector2D center, Vector2D velocity) {
        Vector2D location = center.add(velocity);
        if (location.getY() < paddleRadius) {
            location = new Vector2D(center.getX(), paddleRadius);
        } else if (location.getY() > fieldHeight - paddleRadius) {
            location = new Vector2D(center.getX(), fieldHeight - paddleRadius);
        }
        return location;
    }

    /**
     * Moves and bounces the ball against the paddles and side walls.
     */
    private void moveBall() {
        Vector2D normal = null;
        double speedMultiplier = 1;
        if (ballPosition.getY() <= 0) {
            wallBounces++;
            normal = new Vector2D(0, -1);
        } else if (ballPosition.getY() >= fieldHeight) {
            wallBounces++;
            normal = new Vector2D(0, 1);
        } else if ((ballPosition.getX() <= 0) && (ballPosition.getY() >= leftPaddleCenter.getY() - paddleRadius)
                && (ballPosition.getY() <= leftPaddleCenter.getY() + paddleRadius)) {
            wallBounces = 0;
            leftHits++;
            currentRally++;
            ballPosition = new Vector2D(0, ballPosition.getY());
            final double distanceFromCenter = ballPosition.getY() - leftPaddleCenter.getY();
            final double spin = leftPaddleVelocity.getY() * spinFactor;
            speedMultiplier = paddleElasticCoefficient;
            normal = new Vector2D(1, distanceFromCenter * paddleCurvature + spin).normalize();
        } else if ((ballPosition.getX() >= fieldWidth)
                && (ballPosition.getY() >= rightPaddleCenter.getY() - paddleRadius)
                && (ballPosition.getY() <= rightPaddleCenter.getY() + paddleRadius)) {
            wallBounces = 0;
            rightHits++;
            currentRally++;
            ballPosition = new Vector2D(fieldWidth, ballPosition.getY());
            final double distanceFromCenter = ballPosition.getY() - rightPaddleCenter.getY();
            final double spin = rightPaddleVelocity.getY() * spinFactor;
            speedMultiplier = paddleElasticCoefficient;
            normal = new Vector2D(-1, distanceFromCenter * paddleCurvature + spin).normalize();
        }
        if (normal != null) {
            ballVelocity = reflect(ballVelocity, normal);
            ballVelocity = ballVelocity.scalarMultiply(speedMultiplier);
        }
        ballPosition = ballPosition.add(ballVelocity);
    }

    /**
     * Reflects the vector vec along a given normal. The reflected ray's
     * magnitude will increase if the normal's magnitude is greater than 1.
     *
     * @param vec
     * @param normal
     * @return
     */
    private Vector2D reflect(Vector2D vec, Vector2D normal) {
        final double height = normal.dotProduct(vec);
        final Vector2D difference = normal.scalarMultiply(2 * height);
        return vec.subtract(difference);
    }

    /**
     * Checks if a goal has been scored. If it has, then update the score and
     * serve the ball again.
     */
    private void checkScore() {
        if (ballPosition.getX() <= -10 || ballPosition.getX() >= fieldWidth + 10) {
            if (ballPosition.getX() <= -10) {
                rightScore.set(getRightScore() + 1);
            } else {
                leftScore.set(getLeftScore() + 1);
            }
            if (currentRally > longestRally) {
                longestRally = currentRally;
            }
            totalPaddleHits += currentRally;
            serve();
        }
    }

    public int getLeftScore() {
        return leftScore.get();
    }

    public int getRightScore() {
        return rightScore.get();
    }

    public IntegerProperty leftScoreProperty() {
        return leftScore;
    }

    public IntegerProperty rightScoreProperty() {
        return rightScore;
    }

    public Vector2D getBallPosition() {
        return ballPosition;
    }

    public Vector2D getBallVelocity() {
        return ballVelocity;
    }

    public Vector2D getLeftPaddleCenter() {
        return leftPaddleCenter;
    }

    public Vector2D getLeftPaddleVelocity() {
        return leftPaddleVelocity;
    }

    public Vector2D getRightPaddleCenter() {
        return rightPaddleCenter;
    }

    public Vector2D getRightPaddleVelocity() {
        return rightPaddleVelocity;
    }

    public double getPaddleRadius() {
        return paddleRadius;
    }

    public double getFieldWidth() {
        return fieldWidth;
    }

    public double getFieldHeight() {
        return fieldHeight;
    }

    public void setHeight(int gameHeight) {
        fieldHeight = gameHeight;
    }

    public void setWidth(int gameWidth) {
        fieldWidth = gameWidth;
    }

    public void setPaddleSize(double paddleSize) {
        paddleRadius = paddleSize;
    }

    public void setPaddleCurvature(double paddleCurvature) {
        this.paddleCurvature = paddleCurvature;
    }

    public void setPaddleAcceleration(double paddleAcceleration) {
        this.paddleAcceleration = paddleAcceleration;
    }

    public void setPaddleFriction(double paddleFriction) {
        this.paddleFriction = paddleFriction;
    }

    public void setSpinFactor(double spinFactor) {
        this.spinFactor = spinFactor;
    }

    public void setElasticCoefficient(double elasticCoefficient) {
        paddleElasticCoefficient = elasticCoefficient;
    }

    public void setInitialSpeed(double initialSpeed) {
        initialBallSpeed = initialSpeed;
    }

    public int getLongestRally() {
        return longestRally;
    }

    public int getDeadBalls() {
        return deadBalls;
    }

    public double getAverageRally() {
        return totalPaddleHits / (leftScore.get() + rightScore.get());
    }

    public int getLeftHits() {
        return leftHits;
    }

    public int getRightHits() {
        return rightHits;
    }

}
