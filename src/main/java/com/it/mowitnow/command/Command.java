package com.it.mowitnow.command;

public enum Command {
    D {
        @Override
        public MoveOperation getOperation() {
            return new MoveRightOperation();
        }
    },
    G {
        @Override
        public MoveOperation getOperation() {
            return new MoveLeftOperation();
        }
    },
    A {
        @Override
        public MoveOperation getOperation() {
            return new MoveForwardOperation();
        }
    };

    public abstract MoveOperation getOperation();
}
