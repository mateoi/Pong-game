package com.mateoi.pong;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * An AI player that plays Pong.
 *
 * @author mateo
 *
 */
public class AIPlayer implements Player {

    /** Whether this player is the left one */
    private final boolean isLeft;
    /** A value that points away from the player */
    private final int awayDirection;

    /**
     * Creates a new AI player.
     *
     * @param isLeft
     */
    public AIPlayer(boolean isLeft) {
        this.isLeft = isLeft;
        awayDirection = isLeft ? 1 : -1;
    }

    /**
     * Given a game state, return the position of this player's paddle.
     *
     * @param state
     * @return
     */
    private Vector2D getSelfPosition(PongGame state) {
        return isLeft ? state.getLeftPaddleCenter() : state.getRightPaddleCenter();
    }

    @Override
    public int move(PongGame state) {
        if (Math.signum(state.getBallVelocity().getX()) == awayDirection) {
            return 0;
        }
        double finalY = extendTrajectory(state) % state.getFieldHeight();
        double selfY = getSelfPosition(state).getY();
        return (int) Math.signum(finalY - selfY);
    }

    /**
     * Given a game state, calculates the y-position at which the ball will
     * intersect the player's goal line.
     * 
     * @param state
     * @return
     */
    private double extendTrajectory(PongGame state) {
        Vector2D ballV = state.getBallVelocity();
        Vector2D ballPos = state.getBallPosition();
        double target = isLeft ? 0 : state.getFieldWidth();
        double ticksToTarget = (target - ballPos.getX()) / ballV.getX();
        double finalY = ballPos.getY() + ticksToTarget * ballV.getY();
        return finalY;
    }

}
