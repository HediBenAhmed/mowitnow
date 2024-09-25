package com.it.mowitnow.command;

import com.it.mowitnow.model.LawnDimensions;
import com.it.mowitnow.model.Mower;
import com.it.mowitnow.model.Position;

public class MoveForwardOperation implements MoveOperation {
    @Override
    public void execute(Mower mower, LawnDimensions lawnDimensions) {
        Position oldPosition = mower.getPosition();
        Position newPosition = switch (mower.getDirection()) {
            case N -> validateAndGetPosition(oldPosition.x(), oldPosition.y() + 1, lawnDimensions);
            case E -> validateAndGetPosition(oldPosition.x() + 1, oldPosition.y(), lawnDimensions);
            case W -> validateAndGetPosition(oldPosition.x() - 1, oldPosition.y(), lawnDimensions);
            case S -> validateAndGetPosition(oldPosition.x(), oldPosition.y() - 1, lawnDimensions);
        };

        if (newPosition != null) {
            mower.setPreviousDirection(mower.getDirection());
            mower.setPosition(newPosition);
        } else {
            mower.setDirection(mower.getPreviousDirection());
        }
    }

    private static Position validateAndGetPosition(int x, int y, LawnDimensions lawnDimensions) {
        boolean upperThanLowerLeft = x >= lawnDimensions.lowerLeft().x() && y >= lawnDimensions.lowerLeft().y();
        boolean lowerThanUpperRight = x <= lawnDimensions.upperRight().x() && y <= lawnDimensions.upperRight().y();

        if (upperThanLowerLeft && lowerThanUpperRight) {
            return new Position(x, y);
        }

        return null;
    }
}
