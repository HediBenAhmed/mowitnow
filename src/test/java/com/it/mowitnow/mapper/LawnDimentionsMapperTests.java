package com.it.mowitnow.mapper;

import com.it.mowitnow.model.LawnDimensions;
import com.it.mowitnow.model.Position;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class LawnDimentionsMapperTests {
    @InjectMocks
    private LawnDimensionsMapper lawnDimensionsMapper;

    @ParameterizedTest
    @MethodSource("provideLines")
    void should_map_to_lawnDimensions(String line, LawnDimensions expectedLawnDimension) {
        //When
        LawnDimensions lawnDimensions = lawnDimensionsMapper.mapLine(line);

        //Then
        assertThat(lawnDimensions).isEqualTo(expectedLawnDimension);
    }


    @ParameterizedTest
    @ValueSource(strings = {"", "a a", "1", "11", "5 5 5 abc"})
    void should_throw_exception_when_mapping_with_invalid_line(String line) {

        assertThrows(
                IllegalArgumentException.class,
                () -> {
                    //When
                    lawnDimensionsMapper.mapLine(line);
                }
        );
    }

    private static Stream<Arguments> provideLines() {
        return Stream.of(
                Arguments.of("3 2", new LawnDimensions(new Position(1, 1), new Position(3, 2))),
                Arguments.of("5 5", new LawnDimensions(new Position(1, 1), new Position(5, 5)))
        );
    }
}
