package com.it.mowitnow.batch;

import com.it.mowitnow.model.LawnDimensions;
import com.it.mowitnow.model.Position;
import com.it.mowitnow.utils.FileTestUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@SpringBatchTest
public class SetLawnDimensionsStepTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @ParameterizedTest
    @MethodSource("provideFiles")
    public void should_read_lawnDimensions_from_file(String filePath, Position upperRight) throws IOException {
        //When
        var jobExecution = jobLauncherTestUtils.launchStep("setLawnDimensionsStep",
                new JobParametersBuilder().addString("filePath", FileTestUtils.getFileAbsolutePath(filePath))
                        .addLong("time", System.currentTimeMillis())
                        .toJobParameters());

        //Then
        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");
        assertThat(jobExecution.getExecutionContext().get("lawnDimensions", LawnDimensions.class))
                .extracting(LawnDimensions::lowerLeft, LawnDimensions::upperRight)
                .containsExactly(new Position(1, 1), upperRight);
    }

    @ParameterizedTest
    @ValueSource(strings = {"files/empty.txt", "files/empty_line.txt"})
    void should_throw_exception_when_invalid_file(String filePath) throws IOException {
        //When
        var jobExecution = jobLauncherTestUtils.launchStep("setLawnDimensionsStep",
                new JobParametersBuilder().addString("filePath", FileTestUtils.getFileAbsolutePath(filePath))
                        .addLong("time", System.currentTimeMillis())
                        .toJobParameters());

        //Then
        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("FAILED");
    }

    private static Stream<Arguments> provideFiles() {
        return Stream.of(
                Arguments.of("files/only_one_mower.txt", new Position(5, 5)),
                Arguments.of("files/only_lawnDimension.txt", new Position(2, 2))
        );
    }
}
