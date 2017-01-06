package com.mateoi.pong;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class AIPlayer implements Player {

    private final boolean isLeft;
    private final int awayDirection;

    public AIPlayer(boolean isLeft) {
        this.isLeft = isLeft;
        this.awayDirection = isLeft ? 1 : -1;
    }

    private Vector2D getSelfPosition(PongGame state) {
        return isLeft ? state.getLeftPaddleCenter() : state.getRightPaddleCenter();
    }

    @Override
    public int move(PongGame state) {
        if (state.getBallVelocity().getX() == Math.signum(awayDirection)) {
            return 0;
        }
        double finalY = extendTrajectory(state) % state.getFieldHeight();
        double selfY = getSelfPosition(state).getY();
        return (int) Math.signum(finalY - selfY);
    }

    private double extendTrajectory(PongGame state) {
        Vector2D ballV = state.getBallVelocity();
        Vector2D ballPos = state.getBallPosition();
        double target = isLeft ? 0 : state.getFieldWidth();
        double ticksToTarget = (target - ballPos.getX()) / ballV.getX();
        double finalY = ballPos.getY() + ticksToTarget * ballV.getY();
        return finalY;
    }

}
