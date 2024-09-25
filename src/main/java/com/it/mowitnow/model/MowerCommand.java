package com.it.mowitnow.model;

import org.springframework.util.Assert;

public record MowerCommand(Mower mower, String commands) {
    public MowerCommand {
        Assert.notNull(mower, "mower cannot be null");
        Assert.notNull(commands, "commands cannot be null");
    }
}
