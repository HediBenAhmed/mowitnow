package com.it.mowitnow;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MowerTests {

    @ParameterizedTest
    @MethodSource("provideValidMowerParameters")
    void should_define_mower_by_position_and_direction(String direction, int x, int y) {
        //Given
        Position position = new Position(x, y);
        //When
        Mower mower = new Mower(position, direction);
        //Then
        assertThat(mower).extracting(Mower::position, Mower::direction)
                .containsExactly(position, direction);
    }


    @ParameterizedTest
    @MethodSource("provideInvalidMowerParameters")
    void should_throw_exception_when_define_mower_with_wrong_parameters(String direction, int x, int y) {

        assertThrows(
                IllegalArgumentException.class,
                () -> {
                    //Given
                    Position position = new Position(x, y);

                    //When
                    Mower mower = new Mower(position, direction);
                }
        );
    }

    private static Stream<Arguments> provideValidMowerParameters() {
        return Stream.of(
                Arguments.of("N", 1, 2),
                Arguments.of("E", 2, 3),
                Arguments.of("W", 3, 4),
                Arguments.of("S", 3, 5)
        );
    }

    private static Stream<Arguments> provideInvalidMowerParameters() {
        return Stream.of(
                Arguments.of(null, 3, 5),
                Arguments.of("N", -1, 5),
                Arguments.of("N", 3, -2)
        );
    }
}
