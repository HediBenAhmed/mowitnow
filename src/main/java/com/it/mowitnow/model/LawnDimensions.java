package com.it.mowitnow.model;

import org.springframework.util.Assert;

public record LawnDimensions(Position lowerLeft, Position upperRight) {
    public LawnDimensions {
        Assert.notNull(lowerLeft, "position lowerLeft cannot be null");
        Assert.notNull(upperRight, "position upperRight cannot be null");
        Assert.isTrue(validateLowerAndUpperPositions(lowerLeft, upperRight),
                "invalid lower left and upper right positions");
    }

    private boolean validateLowerAndUpperPositions(Position lowerLeft, Position upperRight) {
        return lowerLeft.x() < upperRight.x() && lowerLeft.y() < upperRight.y();
    }
}
