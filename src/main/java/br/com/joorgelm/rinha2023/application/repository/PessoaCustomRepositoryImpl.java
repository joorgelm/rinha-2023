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

        query.setParameter("termo", termo.replaceAll("(\\s)+", " | "));

        return query.getResultList();
    }

    public String customSave(Pessoa pessoa) {

        Query query = entityManager.createNativeQuery(
                "insert into pessoa (id, apelido, nome, busca, nascimento, stack) " +
                        "values (:id, :apelido, :nome, to_tsvector(:busca), :nascimento, :stack)"
        );
        String stack = pessoaStackConverter.convertToDatabaseColumn(pessoa.getStack());

        UUID pessoaUUID = UUID.randomUUID();
        query.setParameter("id", pessoaUUID);
        query.setParameter("apelido", pessoa.getApelido());
        query.setParameter("nome", pessoa.getNome());
        query.setParameter("busca", pessoa.getApelido() + ' ' + pessoa.getNome() + ' ' + stack);
        query.setParameter("nascimento", pessoa.getNascimento());
        query.setParameter("stack", stack);

//        EntityTransaction transaction = entityManager.getTransaction();

//        transaction.begin();
        query.executeUpdate();
//        transaction.commit();
        return pessoaUUID.toString();
    }

    @Override
    public void customSave(List<Pessoa> pessoas) {
        var stringBuilder = new StringBuilder();
        stringBuilder.append("insert into pessoa (id, apelido, nome, busca, nascimento, stack) values ");

        for (int i = 0; i < pessoas.size(); i++) {
            stringBuilder.append(String.format("(:id%s, :apelido%s, :nome%s, to_tsvector(:busca%s), :nascimento%s, :stack%s)", i, i, i, i, i, i));
            if (i != pessoas.size() - 1) {
                stringBuilder.append(", ");
            }
        }

        var query = entityManager.createNativeQuery(stringBuilder.toString());

        for (int i = 0; i < pessoas.size(); i++) {
            var pessoa = pessoas.get(i);
            String stack = pessoaStackConverter.convertToDatabaseColumn(pessoa.getStack());

            UUID pessoaUUID = UUID.randomUUID();
            query.setParameter("id" + i, pessoaUUID);
            query.setParameter("apelido" + i, pessoa.getApelido());
            query.setParameter("nome" + i, pessoa.getNome());
            query.setParameter("busca" + i, pessoa.getApelido() + ' ' + pessoa.getNome() + ' ' + stack);
            query.setParameter("nascimento" + i, pessoa.getNascimento());
            query.setParameter("stack" + i, stack);
        }

        query.executeUpdate();
    }
}
