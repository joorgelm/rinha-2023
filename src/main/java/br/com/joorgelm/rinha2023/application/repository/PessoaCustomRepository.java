package br.com.joorgelm.rinha2023.application.repository;

import br.com.joorgelm.rinha2023.domain.entity.Pessoa;

import java.util.List;

public interface PessoaCustomRepository {

    List<Pessoa> findAllByTermo(String termo);

    String customSave(Pessoa pessoa);

    void customSave(List<Pessoa> pessoa);

    List<Pessoa> findAllByTermoTsQuery(String termo);
}
