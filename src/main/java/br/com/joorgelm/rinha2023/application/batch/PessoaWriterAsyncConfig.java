package br.com.joorgelm.rinha2023.application.batch;

import br.com.joorgelm.rinha2023.domain.entity.Pessoa;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Future;

@Configuration
public class PessoaWriterAsyncConfig {

    @Bean
    public ItemWriter<Future<Pessoa>> pessoaAsyncWriter(PessoaWriter pessoaWriter) {
        AsyncItemWriter<Pessoa> pessoaAsyncItemWriter = new AsyncItemWriter<>();
        pessoaAsyncItemWriter.setDelegate(pessoaWriter);
        return pessoaAsyncItemWriter;
    }
}
