package br.com.joorgelm.rinha2023.application.service;

import br.com.joorgelm.rinha2023.application.repository.PessoaRepository;
import br.com.joorgelm.rinha2023.domain.entity.Pessoa;
import org.hibernate.ObjectNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CacheService {

    private final Set<String> apelidos;

    private final LinkedHashMap<String, Pessoa> pessoaCache;

    private final SentinelaCacheService sentinelaCacheService;

    private final PessoaRepository pessoaRepository;

    public CacheService(SentinelaCacheService sentinela, PessoaRepository repository) {
        sentinelaCacheService = sentinela;
        apelidos = new HashSet<>(500);
        pessoaCache = new LinkedHashMap<>(500);
        pessoaRepository = repository;
    }

    public boolean apelidoExists(String apelido, boolean sibling) {

        if (sibling) return apelidos.contains(apelido);

        return apelidos.contains(apelido) || sentinelaCacheService.apelidoExists(apelido);
    }

    public void addPessoa(Pessoa pessoa) {

        String apelido = pessoa.getApelido();
        if (apelidoExists(apelido, true))
            throw new DataIntegrityViolationException(String.format("apelido %s já existe", apelido));

        apelidos.add(apelido);
        pessoaCache.put(pessoa.getId().toString(), pessoa);

//        if (apelidos.size() > 500) {
//            pessoaRepository.customSave(pessoaCache.values().stream().toList());
//            // todo limpar cache
//            // todo buscar no bd caso nao encontre na cache
//        }
    }

    public synchronized List<Pessoa> buscaPorTermo(String termo, boolean sibling) {
        List<Pessoa> pessoaList = pessoaCache.values()
                .parallelStream()
                .filter(pessoa -> pessoa.getApelido().contains(termo)
                        || pessoa.getNome().contains(termo)
                        || String.join(" ,", pessoa.getStack()).contains(termo)
                )
                .limit(50L)
                .collect(Collectors.toList());

        if (pessoaList.isEmpty() && sibling) {
            return sentinelaCacheService.buscaPorTermo(termo);
        }

        return pessoaList;
    }

    public Pessoa getPessoa(UUID pessoaId, boolean sibling) {
        if (pessoaCache.containsKey(pessoaId.toString()))
            return pessoaCache.get(pessoaId.toString());

        if (sibling) {
            throw new ObjectNotFoundException(Pessoa.class.getName(), pessoaId);
        }

        Optional<Pessoa> optionalPessoa = Optional.of(sentinelaCacheService.buscaPorId(pessoaId.toString()));

        if (optionalPessoa.isPresent()) {
            return optionalPessoa.get();
        }

        throw new ObjectNotFoundException(Pessoa.class.getName(), pessoaId);
    }

    public int contagem(boolean sibling) {

        if (sibling) return pessoaCache.size();

        return pessoaCache.size() + sentinelaCacheService.contagem();
    }
}
