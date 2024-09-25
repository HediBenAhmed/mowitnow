package com.it.mowitnow;

import org.springframework.util.Assert;

import java.io.Serializable;

public record Position(int x, int y) implements Serializable {
    public Position {
        Assert.isTrue(x > 0, "position x cannot be negative");
        Assert.isTrue(y > 0, "position y cannot be negative");
    }
}
