package com.it.mowitnow.command;

import com.it.mowitnow.model.Direction;
import com.it.mowitnow.model.LawnDimensions;
import com.it.mowitnow.model.Mower;

public class MoveLeftOperation implements MoveOperation {
    @Override
    public void execute(Mower mower, LawnDimensions lawnDimensions) {
        Direction previousDirection = mower.getDirection();
        Direction newDirection = switch (previousDirection) {
            case N -> Direction.W;
            case E -> Direction.N;
            case W -> Direction.S;
            case S -> Direction.E;
        };
        mower.setPreviousDirection(previousDirection);
        mower.setDirection(newDirection);
    }
}
