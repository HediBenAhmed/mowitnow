package com.it.mowitnow.batch;

import com.it.mowitnow.mapper.MowerCommandMapper;
import com.it.mowitnow.model.Direction;
import com.it.mowitnow.model.Mower;
import com.it.mowitnow.model.MowerCommand;
import com.it.mowitnow.model.Position;
import com.it.mowitnow.utils.FileTestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MowerCommandReaderTests {

    private final MowerCommandMapper mowerCommandMapper = new MowerCommandMapper();

    @ParameterizedTest
    @MethodSource("provideFiles")
    public void should_read_mowerCommand_from_file(String filePath, MowerCommand expectedMowerCommand) throws IOException {
        //Given
        MowerCommandReader mowerCommandReader = new MowerCommandReader(FileTestUtils.getFileAbsolutePath(filePath), mowerCommandMapper);

        //When
        MowerCommand currentMowerCommand = mowerCommandReader.read();

        //Then
        assertThat(currentMowerCommand).isEqualTo(expectedMowerCommand);
    }

    @Test
    void should_throw_exception_when_invalid_filePath() {
        //When
        assertThrows(
                FileNotFoundException.class, () -> {
                    MowerCommandReader mowerCommandReader = new MowerCommandReader("/invalid/path/file.txt", mowerCommandMapper);
                    mowerCommandReader.read();
                }
        );
    }

    private static Stream<Arguments> provideFiles() {
        return Stream.of(
                Arguments.of("files/empty.txt", null),
                Arguments.of("files/empty_line.txt", null),
                Arguments.of("files/only_lawnDimension.txt", null),
                Arguments.of("files/complete.txt", new MowerCommand(new Mower(new Position(1, 2), Direction.N), "GAGAGAGAA")),
                Arguments.of("files/only_one_mower.txt", new MowerCommand(new Mower(new Position(1, 2), Direction.N), "GAGAGAGAA"))
        );
    }
}
