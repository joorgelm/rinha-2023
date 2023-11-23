package br.com.joorgelm.rinha2023.application.repository;

import br.com.joorgelm.rinha2023.domain.converter.PessoaStackConverter;
import br.com.joorgelm.rinha2023.domain.entity.Pessoa;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import java.util.List;
import java.util.UUID;

public class PessoaCustomRepositoryImpl implements PessoaCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private final PessoaStackConverter pessoaStackConverter;

    public PessoaCustomRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.pessoaStackConverter = new PessoaStackConverter();
    }

    public List<Pessoa> findAllByTermo(String termo) {
        Query query = entityManager.createNativeQuery("select * from pessoa p where " +
                "p.nome ilike concat('%',:termo,'%') or " +
                "p.apelido ilike concat('%',:termo,'%') or " +
                "p.stack ilike concat('%',:termo,'%')", Pessoa.class);

        query.setParameter("termo", termo);

        return query.getResultList();
    }

    public List<Pessoa> findAllByTermoTsQuery(String termo) {
        Query query = entityManager.createNativeQuery("select * from pessoa p where " +
                "p.busca @@ to_tsquery( :termo )", Pessoa.class);

        query.setParameter("termo", termo);

        return query.getResultList();
    }

    public void customSave(Pessoa pessoa) {

        Query query = entityManager.createNativeQuery(
                "insert into pessoa (id, apelido, nome, busca, nascimento, stack) " +
                        "values (:id, :apelido, :nome, to_tsvector(:busca), :nascimento, :stack)"
        );
        String stack = pessoaStackConverter.convertToDatabaseColumn(pessoa.getStack());

        query.setParameter("id", UUID.randomUUID());
        query.setParameter("apelido", pessoa.getApelido());
        query.setParameter("nome", pessoa.getNome());
        query.setParameter("busca", pessoa.getApelido() + ' ' + pessoa.getNome() + ' ' + stack);
        query.setParameter("nascimento", pessoa.getNascimento());
        query.setParameter("stack", stack);

//        EntityTransaction transaction = entityManager.getTransaction();

//        transaction.begin();
        query.executeUpdate();
//        transaction.commit();
    }
}
