package com.it.mowitnow.model;

import org.springframework.util.Assert;

import java.util.Objects;

public class Mower {
    private Position position;
    private Direction direction;
    private Direction PreviousDirection;

    public Mower(Position position, Direction direction) {
        Assert.notNull(position, "position cannot be null");
        Assert.notNull(direction, "Direction cannot be null");
        this.position = position;
        this.direction = direction;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Direction getPreviousDirection() {
        return PreviousDirection;
    }

    public void setPreviousDirection(Direction previousDirection) {
        PreviousDirection = previousDirection;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mower mower = (Mower) o;
        return Objects.equals(position, mower.position) && direction == mower.direction && PreviousDirection == mower.PreviousDirection;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, direction, PreviousDirection);
    }
}
