package br.com.joorgelm.rinha2023.application.batch;

import br.com.joorgelm.rinha2023.application.service.CacheService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PessoaProcessorConfig {

    @Bean
    public PessoaProcessor pessoaProcessor(CacheService cacheService) {
        return new PessoaProcessor(cacheService);
    }
}
