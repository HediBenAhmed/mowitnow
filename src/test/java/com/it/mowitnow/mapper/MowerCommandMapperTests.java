package com.it.mowitnow.mapper;

import com.it.mowitnow.model.Direction;
import com.it.mowitnow.model.Mower;
import com.it.mowitnow.model.MowerCommand;
import com.it.mowitnow.model.Position;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class MowerCommandMapperTests {
    @InjectMocks
    private MowerCommandMapper mowerCommandMapper;

    @ParameterizedTest
    @MethodSource("provideLines")
    void should_map_to_mowerCommand(String line1, String line2, MowerCommand expectedMowerCommand) {
        //When
        MowerCommand mowerCommand = mowerCommandMapper.map(Arrays.asList(line1, line2));

        //Then
        assertThat(mowerCommand).isEqualTo(expectedMowerCommand);
    }


    @ParameterizedTest
    @MethodSource("provideInvalidLines")
    void should_throw_exception_when_mapping_with_invalid_line(String line1, String line2) {

        assertThrows(
                IllegalArgumentException.class,
                () -> {
                    //When
                    mowerCommandMapper.map(Arrays.asList(line1, line2));
                }
        );
    }

    private static Stream<Arguments> provideLines() {
        return Stream.of(
                Arguments.of("1 2 N", "GAGAGAGAA", new MowerCommand(new Mower(new Position(1, 2), Direction.N), "GAGAGAGAA")),
                Arguments.of("3 3 E", "AADAADADDA", new MowerCommand(new Mower(new Position(3, 3), Direction.E), "AADAADADDA"))
        );
    }

    private static Stream<Arguments> provideInvalidLines() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of("3 3 E", null),
                Arguments.of(null, "AADAADADDA")
        );
    }
}
