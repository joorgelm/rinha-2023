package br.com.joorgelm.rinha2023.application.batch;


import br.com.joorgelm.rinha2023.application.service.CacheService;
import br.com.joorgelm.rinha2023.domain.entity.Pessoa;
import org.springframework.batch.item.ItemProcessor;
import reactor.util.annotation.NonNull;
public class PessoaProcessor implements ItemProcessor<Pessoa, Pessoa> {

    private final CacheService cacheService;

    public PessoaProcessor(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Override
    public Pessoa process(@NonNull Pessoa pessoas) {
        return cacheService.removeLast();
    }
}
