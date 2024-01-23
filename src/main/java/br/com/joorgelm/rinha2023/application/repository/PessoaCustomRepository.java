package br.com.joorgelm.rinha2023.application.repository;

import br.com.joorgelm.rinha2023.domain.entity.Pessoa;

import java.util.List;
import java.util.Optional;

public interface PessoaCustomRepository {

    List<Pessoa> findAllByTermo(String termo);

    String customSave(Pessoa pessoa);

    void customSave(List<Pessoa> pessoa);

    List<Pessoa> findAllByTermoTsQuery(String termo);

    Optional<Pessoa> findByApelido(String apelido);

}
