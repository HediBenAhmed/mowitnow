package com.it.mowitnow;

import org.springframework.util.Assert;

public record Mower(Position position, String direction) {
    public Mower {
        Assert.notNull(position, "position cannot be null");
        Assert.notNull(direction, "Direction cannot be null");
    }
}
