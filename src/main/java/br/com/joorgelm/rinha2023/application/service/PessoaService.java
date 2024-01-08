package br.com.joorgelm.rinha2023.application.service;

import br.com.joorgelm.rinha2023.application.messaging.PessoaProducer;
import br.com.joorgelm.rinha2023.domain.entity.Pessoa;
import com.google.gson.Gson;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PessoaService {
    private final CacheService cacheService;

    private final PessoaProducer pessoaProducer;

    public PessoaService(CacheService cacheService, PessoaProducer pessoaProducer) {
        this.cacheService = cacheService;
        this.pessoaProducer = pessoaProducer;
    }

    public String cadastrarPessoa(Pessoa pessoa) {
        UUID pessoaUUID = UUID.randomUUID();
        pessoa.setId(pessoaUUID);
        pessoa.validarDados();

        if (Optional.ofNullable(pessoa.getStack()).isEmpty()) pessoa.setStack(Collections.emptyList());

        cacheService.addPessoa(pessoa);
        pessoaProducer.sendMessage(new Gson().toJson(pessoa));

        return pessoaUUID.toString();
    }

    public Pessoa buscarPorId(UUID pessoaId, boolean sibling) {
        return Optional.of(cacheService.getPessoa(pessoaId, sibling))
                .orElseThrow(() -> new ObjectNotFoundException(Pessoa.class.getName(), pessoaId));
    }

    public List<Pessoa> buscaPorTermo(String t, boolean sibling) {
        return cacheService.buscaPorTermo(t, sibling);
    }

    public long contagemPessoas(boolean sibling) {
        return cacheService.contagem(sibling);
    }

    public Boolean buscarPorApelido(String apelido, boolean sibling) {
        return cacheService.apelidoExists(apelido, sibling);
    }
}
