package com.it.mowitnow.batch;

import com.it.mowitnow.model.LawnDimensions;
import com.it.mowitnow.model.Mower;
import com.it.mowitnow.model.MowerCommand;
import com.it.mowitnow.service.MoveOperationService;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class MowerCommandProcessor implements ItemProcessor<MowerCommand, Mower> {
    private final LawnDimensions lawnDimension;
    private final MoveOperationService moveOperationService;

    public MowerCommandProcessor(@Value("#{stepExecution.jobExecution}") JobExecution jobExecution,
                                 MoveOperationService moveOperationService) {
        this.lawnDimension = jobExecution.getExecutionContext().get("lawnDimensions", LawnDimensions.class);
        this.moveOperationService = moveOperationService;
    }

    @Override
    public Mower process(MowerCommand item) throws Exception {
        moveOperationService.execute(item, lawnDimension);
        return item.mower();
    }
}
