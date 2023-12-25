package br.com.joorgelm.rinha2023.application.service;

import br.com.joorgelm.rinha2023.application.batch.dispatcher.PessoaWriterJobDispatcher;
import br.com.joorgelm.rinha2023.application.repository.PessoaRepository;
import br.com.joorgelm.rinha2023.domain.entity.Pessoa;
import jakarta.transaction.Transactional;
import org.hibernate.ObjectNotFoundException;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PessoaService {

    private final PessoaRepository pessoaRepository;
    private final CacheService cacheService;

    private final PessoaWriterJobDispatcher pessoaWriterJobDispatcher;

    public PessoaService(PessoaRepository pessoaRepository, CacheService cacheService, PessoaWriterJobDispatcher pessoaWriterJobDispatcher) {
        this.pessoaRepository = pessoaRepository;
        this.cacheService = cacheService;
        this.pessoaWriterJobDispatcher = pessoaWriterJobDispatcher;
    }

    @Transactional
    public String cadastrarPessoa(Pessoa pessoa) {
        UUID pessoaUUID = UUID.randomUUID();
        pessoa.setId(pessoaUUID);
        pessoa.validarDados();

        if (Optional.ofNullable(pessoa.getStack()).isEmpty()) pessoa.setStack(Collections.emptyList());

        cacheService.addPessoa(pessoa);

        if (cacheService.contagem(false) > 20) {

            try {
                pessoaWriterJobDispatcher.run();
            } catch (JobInstanceAlreadyCompleteException e) {
                throw new RuntimeException(e);
            } catch (JobExecutionAlreadyRunningException e) {
                throw new RuntimeException(e);
            } catch (JobParametersInvalidException e) {
                throw new RuntimeException(e);
            } catch (JobRestartException e) {
                throw new RuntimeException(e);
            }
        }

        // todo: parar de salvar no momento do cadastro e montar um bulkinsert
        return pessoaUUID.toString();
    }

    public Pessoa buscarPorId(UUID pessoaId, boolean sibling) {

        return Optional.of(cacheService.getPessoa(pessoaId, sibling))
                .orElseThrow(() -> new ObjectNotFoundException(Pessoa.class.getName(), pessoaId));
    }

    // apelido, nome, elementos da stack
    public List<Pessoa> buscaPorTermo(String t, boolean sibling) {
        return cacheService.buscaPorTermo(t, sibling);
//        return pessoaRepository.findAllByTermoTsQuery(t);
    }

    public long contagemPessoas(boolean sibling) {
        return cacheService.contagem(sibling);
    }

    public Boolean buscarPorApelido(String apelido, boolean sibling) {
        return cacheService.apelidoExists(apelido, sibling);
    }
}
