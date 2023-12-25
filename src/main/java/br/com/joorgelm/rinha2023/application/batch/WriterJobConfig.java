package br.com.joorgelm.rinha2023.application.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WriterJobConfig {

    @Bean
    public Job writerJob(
            JobRepository jobRepository,
            @Qualifier("writerAsyncStep") Step writerAsyncStep
    ) {
        return new JobBuilder("writerJob", jobRepository)
                .start(writerAsyncStep)
                .build();
    }
}
