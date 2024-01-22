package br.com.joorgelm.rinha2023.application.service;

import br.com.joorgelm.rinha2023.application.repository.PessoaCustomRepository;
import br.com.joorgelm.rinha2023.application.repository.PessoaCustomRepositoryImpl;
import br.com.joorgelm.rinha2023.domain.entity.Pessoa;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.hibernate.ObjectNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

@Service
public class CacheService {

    private final Set<String> apelidos;

    private final ConcurrentHashMap<String, Pessoa> pessoaCache;

    private final ConcurrentLinkedDeque<Pessoa> pessoaDeque;

    private final SentinelaCacheService sentinelaCacheService;

    private final PessoaCustomRepository pessoaCustomRepository;


    public CacheService(SentinelaCacheService sentinela, EntityManager entityManager) {
        sentinelaCacheService = sentinela;
        apelidos = new HashSet<>(22000);
        pessoaCache = new ConcurrentHashMap<>(22000);
        pessoaCustomRepository = new PessoaCustomRepositoryImpl(entityManager);
        pessoaDeque = new ConcurrentLinkedDeque<>();
    }

    public boolean apelidoExists(String apelido, boolean sibling) {
        if (sibling) return apelidos.contains(apelido);

        return apelidos.contains(apelido) || sentinelaCacheService.apelidoExists(apelido);
    }

    public void addPessoa(Pessoa pessoa) {

        String apelido = pessoa.getApelido();
        if (apelidoExists(apelido, false))
            throw new DataIntegrityViolationException(String.format("apelido %s já existe", apelido));

        apelidos.add(apelido);
        pessoaCache.put(pessoa.getId().toString(), pessoa);
        pessoaDeque.add(pessoa);
    }

    public List<Pessoa> buscaPorTermo(String termo, boolean sibling) {
        List<Pessoa> pessoaList;
        synchronized (pessoaCache) {
            pessoaList = pessoaCache.values()
                    .parallelStream()
                    .filter(pessoa -> pessoa.getApelido().contains(termo)
                            || pessoa.getNome().contains(termo)
                            || String.join(" ,", pessoa.getStack()).contains(termo)
                    )
                    .limit(50L)
                    .toList();
        }

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

        return sentinelaCacheService.buscaPorId(pessoaId.toString())
                .orElseThrow(
                    () -> new ObjectNotFoundException(Pessoa.class.getName(), pessoaId)
                );
    }

    public int contagem(boolean sibling) {

        if (sibling) return pessoaCache.size();

        return pessoaCache.size() + sentinelaCacheService.contagem();
    }

    @Scheduled(fixedRate = 5000)
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void scheduledSave() {
        synchronized (pessoaDeque) {
            if (pessoaDeque.isEmpty()) return;
            List<Pessoa> pessoas = pessoaDeque.stream()
                    .collect(Collectors.toUnmodifiableList());
            pessoaCustomRepository.customSave(pessoas);
            pessoaDeque.clear();
        }
    }
}
