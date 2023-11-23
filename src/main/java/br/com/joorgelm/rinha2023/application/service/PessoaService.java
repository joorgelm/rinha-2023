package br.com.joorgelm.rinha2023.application.service;

import br.com.joorgelm.rinha2023.application.repository.PessoaRepository;
import br.com.joorgelm.rinha2023.domain.entity.Pessoa;
import jakarta.transaction.Transactional;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PessoaService {

    private final PessoaRepository pessoaRepository;

    public PessoaService(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    @Transactional
    public Pessoa cadastrarPessoa(Pessoa pessoa) {
        pessoa.validarDados();
        pessoaRepository.customSave(pessoa);
        return pessoa;
    }

    public Pessoa buscarPorId(UUID pessoaId) {
        return pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new ObjectNotFoundException(Pessoa.class.getName(), pessoaId));
    }

    // apelido, nome, elementos da stack
    public List<Pessoa> buscaPorTermo(String t) {
        return pessoaRepository.findAllByTermoTsQuery(t);
    }

    public long contagemPessoas() {
        return pessoaRepository.count();
    }
}
