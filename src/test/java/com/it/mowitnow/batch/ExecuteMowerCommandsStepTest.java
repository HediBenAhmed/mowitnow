package com.it.mowitnow.batch;

import com.it.mowitnow.model.Direction;
import com.it.mowitnow.model.LawnDimensions;
import com.it.mowitnow.model.Mower;
import com.it.mowitnow.model.Position;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@SpringBatchTest
public class ExecuteMowerCommandsStepTest {
    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @ParameterizedTest
    @MethodSource("provideFiles")
    public void should_execute_mowerCommands_from_file(String filePath, Position lastPosition, Direction lastDirection) throws IOException {
        //When
        var jobExecution = jobLauncherTestUtils.launchStep("executeMowerCommandsStep",
                new JobParametersBuilder().addString("filePath", getFileAbsolutePath(filePath))
                        .toJobParameters(),
                new ExecutionContext(Map.of("lawnDimensions", new LawnDimensions(new Position(1, 1), new Position(5, 5))))
        );

        //Then
        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");
    }

    @ParameterizedTest
    @ValueSource(strings = {"files/empty.txt", "files/empty_line.txt", "file/only_lawnDimension.txt"})
    void should_throw_exception_when_invalid_file(String filePath) throws IOException {
        //When
        var jobExecution = jobLauncherTestUtils.launchStep("executeMowerCommandsStep",
                new JobParametersBuilder().addString("filePath", getFileAbsolutePath(filePath))
                        .toJobParameters(),
                new ExecutionContext(Map.of("lawnDimensions", new LawnDimensions(new Position(1, 1), new Position(5, 5))))
        );

        //Then
        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("FAILED");
    }

    private static Stream<Arguments> provideFiles() {
        return Stream.of(
                Arguments.of("files/only_one_mower.txt", new Position(5, 5)),
                Arguments.of("files/complete.txt", new Position(2, 2))
        );
    }

    private String getFileAbsolutePath(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);
        return resource.getFile().getAbsolutePath();
    }
}
