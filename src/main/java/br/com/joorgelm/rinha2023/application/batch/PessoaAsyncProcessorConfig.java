package br.com.joorgelm.rinha2023.application.batch;

/*
* @Configuration
public class ClassificarFamiliaAsyncProcessorConfig {

    @Bean
    public ItemProcessor<Familia, Future<Familia>> classificarFamiliaAsyncProcessor(
            ClassificarFamiliaProcessor classificarFamiliaProcessor,
            TaskExecutor taskExecutor
    ) {
        AsyncItemProcessor<Familia, Familia> familiaAsyncItemProcessor = new AsyncItemProcessor<>();
        familiaAsyncItemProcessor.setTaskExecutor(taskExecutor);
        familiaAsyncItemProcessor.setDelegate(classificarFamiliaProcessor);
        return familiaAsyncItemProcessor;
    }
}
* */

import br.com.joorgelm.rinha2023.domain.entity.Pessoa;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

@Configuration
public class PessoaAsyncProcessorConfig {

    @Bean
    public AsyncItemProcessor<Pessoa, Pessoa> pessoaAsyncProcessor(
            PessoaProcessor pessoaProcessor,
            TaskExecutor taskExecutor
    ) {
        AsyncItemProcessor<Pessoa, Pessoa> pessoaAsyncItemProcessor = new AsyncItemProcessor<>();
        pessoaAsyncItemProcessor.setTaskExecutor(taskExecutor);
        pessoaAsyncItemProcessor.setDelegate(pessoaProcessor);
        return pessoaAsyncItemProcessor;
    }
}
