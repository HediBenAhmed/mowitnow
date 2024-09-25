package com.it.mowitnow;

import com.it.mowitnow.batch.ReadLawnDimensionTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class MowItNowJobConfig {

    @Bean
    public Job mowItNowJob(Step setLawnDimensionsStep,
                           JobRepository jobRepository) {
        return new JobBuilder("mowItNowJob", jobRepository)
                .start(setLawnDimensionsStep)
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
}
