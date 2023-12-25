package br.com.joorgelm.rinha2023.application.batch;

import br.com.joorgelm.rinha2023.application.repository.PessoaRepository;
import br.com.joorgelm.rinha2023.domain.entity.Pessoa;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Future;

@Configuration
public class PessoaWriterConfig {

    @Bean PessoaWriter pessoaWriter(PessoaRepository pessoaRepository) {
        PessoaWriter pessoaWriter = new PessoaWriter();
        pessoaWriter.setRepository(pessoaRepository);
        return pessoaWriter;
    }
}
