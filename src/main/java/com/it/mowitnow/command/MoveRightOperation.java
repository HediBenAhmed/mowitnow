package com.it.mowitnow.command;

import com.it.mowitnow.model.Direction;
import com.it.mowitnow.model.LawnDimensions;
import com.it.mowitnow.model.Mower;

public class MoveRightOperation implements MoveOperation {
    @Override
    public void execute(Mower mower, LawnDimensions lawnDimension) {
        Direction previousDirection = mower.getDirection();
        Direction newDirection = switch (previousDirection) {
            case N -> Direction.E;
            case E -> Direction.S;
            case W -> Direction.N;
            case S -> Direction.W;
        };
        mower.setPreviousDirection(previousDirection);
        mower.setDirection(newDirection);
    }
}
