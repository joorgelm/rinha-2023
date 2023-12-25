package br.com.joorgelm.rinha2023.application.batch;

import br.com.joorgelm.rinha2023.application.repository.PessoaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PessoaReaderConfig {

    @Bean
    public PessoaReader pessoaReader(PessoaRepository pessoaRepository) {
        PessoaReader pessoaReader = new PessoaReader();
        return pessoaReader;
    }
}
