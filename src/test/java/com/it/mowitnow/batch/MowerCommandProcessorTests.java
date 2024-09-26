package com.it.mowitnow.batch;

import com.it.mowitnow.model.*;
import com.it.mowitnow.service.MoveOperationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.item.ExecutionContext;

import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;

@ExtendWith(MockitoExtension.class)
public class MowerCommandProcessorTests {
    @Mock
    private MoveOperationService moveOperationService;

    @Mock
    private JobExecution jobExecution;

    @Test
    public void should_process_mowerCommand() throws Exception {
        //Given
        MowerCommand mowerCommand = new MowerCommand(new Mower(new Position(1, 2), Direction.N), "A");
        LawnDimensions lawnDimensions = new LawnDimensions(new Position(1, 1), new Position(5, 5));
        given(jobExecution.getExecutionContext()).willReturn(new ExecutionContext(Map.of("lawnDimensions", new LawnDimensions(new Position(1, 1), new Position(5, 5)))));
        MowerCommandProcessor mowerCommandProcessor = new MowerCommandProcessor(jobExecution, moveOperationService);

        doAnswer((Answer<Void>) invocation -> {
                    var mower = invocation.getArgument(0, MowerCommand.class).mower();
                    mower.setPosition(new Position(mower.getPosition().x() + 1, mower.getPosition().y()));
                    return null;
                }
        ).when(moveOperationService).execute(mowerCommand, lawnDimensions);

        //When
        Mower mower = mowerCommandProcessor.process(mowerCommand);

        //Then
        assertThat(mower).extracting(m -> m.getPosition().x(), m -> m.getPosition().x())
                .containsExactly(2, 2);
    }

    @Test
    void should_throw_exception_when_null_command() {
        //Given
        given(jobExecution.getExecutionContext()).willReturn(new ExecutionContext(Map.of("lawnDimensions", new LawnDimensions(new Position(1, 1), new Position(5, 5)))));
        MowerCommandProcessor mowerCommandProcessor = new MowerCommandProcessor(jobExecution, moveOperationService);

        //When
        assertThrows(
                IllegalArgumentException.class, () -> {
                    mowerCommandProcessor.process(null);
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
