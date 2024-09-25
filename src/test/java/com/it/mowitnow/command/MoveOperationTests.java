package com.it.mowitnow.command;

import com.it.mowitnow.model.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MoveOperationTests {

    @ParameterizedTest
    @MethodSource("provideCommands")
    void should_execute_commands(Mower mower, String commands, Position expectedPosition, Direction expectedDirection) {
        //Given
        LawnDimensions lawnDimensions = new LawnDimensions(new Position(0, 0), new Position(5, 5));
        MowerCommand mowerCommand = new MowerCommand(mower, commands);
        MoveOperation moveOperation = new MoveOperation();

        //When
        moveOperation.execute(mowerCommand, lawnDimensions);

        //Then
        assertThat(mower).extracting(Mower::position, Mower::direction)
                .containsExactly(expectedPosition, expectedDirection);
    }


    @ParameterizedTest
    @MethodSource("provideInvalidCommands")
    void should_throw_exception_when_executing_invalid_commands(Position upperRight, Mower mower, String commands) {

        assertThrows(
                IllegalArgumentException.class,
                () -> {
                    //Given
                    LawnDimensions lawnDimensions = new LawnDimensions(new Position(0, 0), upperRight);
                    MowerCommand mowerCommand = new MowerCommand(mower, commands);
                    MoveOperation moveOperation = new MoveOperation();

                    //When
                    moveOperation.execute(mowerCommand, lawnDimensions);
                }
        );
    }

    private static Stream<Arguments> provideCommands() {
        return Stream.of(
                Arguments.of(new Mower(new Position(1, 1), Direction.N), "ADAG", new Position(1, 1), Direction.N),
                Arguments.of(new Mower(new Position(1, 1), Direction.N), "AA", new Position(1, 1), Direction.N),
                Arguments.of(new Mower(new Position(1, 2), Direction.N), "GAGAGAGAA", new Position(1, 3), Direction.N),
                Arguments.of(new Mower(new Position(3, 3), Direction.E), "AADAADADDA", new Position(5, 1), Direction.E)
        );
    }

    private static Stream<Arguments> provideInvalidCommands() {
        return Stream.of(
                Arguments.of(null, new Mower(new Position(1, 1), Direction.N), "AAAAA"),
                Arguments.of(new Position(3, 3), null, "AAAAA"),
                Arguments.of(new Position(3, 3), new Mower(new Position(1, 1), Direction.N), null),
                Arguments.of(new Position(3, 3), new Mower(new Position(1, 1), Direction.N), "XYZ"),
                Arguments.of(new Position(3, 3), new Mower(new Position(1, 1), Direction.N), "agd")
        );
    }
}
