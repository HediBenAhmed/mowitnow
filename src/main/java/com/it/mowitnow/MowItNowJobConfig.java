package com.it.mowitnow;

import com.it.mowitnow.batch.MowerCommandProcessor;
import com.it.mowitnow.batch.MowerCommandReader;
import com.it.mowitnow.batch.MowerWriter;
import com.it.mowitnow.batch.ReadLawnDimensionTasklet;
import com.it.mowitnow.model.Mower;
import com.it.mowitnow.model.MowerCommand;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class MowItNowJobConfig {

    @Bean
    public Job mowItNowJob(Step setLawnDimensionsStep,
                           Step executeMowerCommandsStep,
                           JobRepository jobRepository) {
        return new JobBuilder("mowItNowJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(setLawnDimensionsStep)
                .next(executeMowerCommandsStep)
                .build();
    }

    @Bean
    public Step setLawnDimensionsStep(ReadLawnDimensionTasklet readLawnDimensionTasklet,
                                      JobRepository jobRepository,
                                      PlatformTransactionManager transactionManager) {
        return new StepBuilder("setLawnDimensionsStep", jobRepository)
                .tasklet(readLawnDimensionTasklet, transactionManager)
                .build();
    }

    @Bean
    public Step executeMowerCommandsStep(MowerCommandReader mowerCommandReader,
                                         MowerCommandProcessor mowerCommandProcessor,
                                         MowerWriter mowerWriter,
                                         JobRepository jobRepository,
                                         PlatformTransactionManager transactionManager) {
        return new StepBuilder("executeMowerCommandsStep", jobRepository)
                .<MowerCommand, Mower>chunk(1, transactionManager)
                .reader(mowerCommandReader)
                .processor(mowerCommandProcessor)
                .writer(mowerWriter)
                .build();
    }
}
