package com.it.mowitnow.model;

import org.springframework.util.Assert;

public record Mower(Position position, Direction direction) {
    public Mower {
        Assert.notNull(position, "position cannot be null");
        Assert.notNull(direction, "Direction cannot be null");
    }
}
