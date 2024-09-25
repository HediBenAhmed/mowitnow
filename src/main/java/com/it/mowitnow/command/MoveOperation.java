package com.it.mowitnow.command;

import com.it.mowitnow.model.LawnDimensions;
import com.it.mowitnow.model.Mower;

@FunctionalInterface
public interface MoveOperation {
    void execute(Mower mower, LawnDimensions lawnDimensions);
}
