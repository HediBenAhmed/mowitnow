package com.it.mowitnow.batch;

import com.it.mowitnow.mapper.LawnDimensionsMapper;
import com.it.mowitnow.model.LawnDimensions;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

@Component
@StepScope
public class ReadLawnDimensionTasklet implements Tasklet {

    private final String filePath;
    private final LawnDimensionsMapper lawnDimensionsMapper;

    public ReadLawnDimensionTasklet(@Value("#{jobParameters['filePath']}") String filePath,
                                    LawnDimensionsMapper lawnDimensionsMapper) {
        Assert.notNull(filePath, "the argument filePath is mandatory");
        this.filePath = filePath;
        this.lawnDimensionsMapper = lawnDimensionsMapper;
    }


    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String firstLine =  getFirstLine();
        LawnDimensions lawnDimensions = lawnDimensionsMapper.mapLine(firstLine);
        chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().put("lawnDimensions", lawnDimensions);
        return RepeatStatus.FINISHED;
    }

    private String getFirstLine() throws FileNotFoundException {
        Scanner scan = null;
        try {
            File file = new File(filePath);
            scan = new Scanner(file);
            if (scan.hasNextLine()) {
                return scan.nextLine();
            }

            return null;
        } finally {
            if (scan != null) {
                scan.close();
            }
        }
    }
}
