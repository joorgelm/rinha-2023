package br.com.joorgelm.rinha2023.application.batch;

import br.com.joorgelm.rinha2023.domain.entity.Pessoa;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;
import java.util.concurrent.Future;

@Configuration
public class WriterStep {

    @Bean
    public Step writerAsyncStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            ItemProcessor<Pessoa, Future<Pessoa>> pessoaAsyncProcessor,
            ItemWriter<Future<Pessoa>> pessoaWriter
    ) {
        return new StepBuilder("writerAsyncStep", jobRepository)
                .<Pessoa, Future<Pessoa>>chunk(1, transactionManager)
                .processor(pessoaAsyncProcessor)
                .writer(pessoaWriter)
                .build();
    }
}
