package com.it.mowitnow.batch;

import com.it.mowitnow.model.LawnDimensions;
import com.it.mowitnow.model.Position;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@SpringBatchTest
public class SetLawnDimensionsStepTest {
    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @ParameterizedTest
    @MethodSource("provideFiles")
    public void should_read_lawnDimensions_from_file(String filePath, Position upperRight) throws IOException {
        var jobExecution = jobLauncherTestUtils.launchStep("setLawnDimensionsStep",
                new JobParametersBuilder().addString("filePath", getFileAbsolutePath(filePath))
                        .toJobParameters());

        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");
        assertThat(jobExecution.getExecutionContext().get("lawnDimensions", LawnDimensions.class))
                .extracting(LawnDimensions::lowerLeft, LawnDimensions::upperRight)
                .containsExactly(new Position(1, 1), upperRight);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "empty.txt", "empty_line.txt"})
    void should_throw_exception_when_invalid_file(String filePath) {

        assertThrows(
                Exception.class,
                () -> {
                    //When
                    jobLauncherTestUtils.launchStep("setLawnDimensionsStep",
                            new JobParametersBuilder().addString("filePath", getFileAbsolutePath(filePath))
                                    .toJobParameters());
                }
        );
    }

    private static Stream<Arguments> provideFiles() {
        return Stream.of(
                Arguments.of("only_one_mower.txt", new Position(5, 5)),
                Arguments.of("only_lawnDimension.txt", new Position(2, 2))
        );
    }

    private String getFileAbsolutePath(String path) throws IOException {
        var resource = resourceLoader.getResource("classpath:" + path);
        return resource.getFile().getAbsolutePath();
    }
}
