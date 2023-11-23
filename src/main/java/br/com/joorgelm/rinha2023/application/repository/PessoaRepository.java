package br.com.joorgelm.rinha2023.application.repository;

import br.com.joorgelm.rinha2023.domain.entity.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, UUID>, PessoaCustomRepository {

//    @Query("select p from Pessoa p " +
//            "where p.apelido ilike %?1% or " +
//            "p.nome ilike %?1% or " +
//            "elements(p.stack) like %?1%"
//    )
//    List<Pessoa> findAllByTerm(String t);


    /*
SELECT * FROM pessoa p
WHERE p.nome ilike '%mario%' or p.stack ilike '%nods%';
    */

//    @Query(value=
//            "select * from pessoa p " +
//            "where p.apelido ilike '%:termo%' or " +
//            "p.nome ilike '%:termo%' or " +
//            "p.stack ilike '%:termo%'",
//            nativeQuery = true
//    )
//    List<Pessoa> findAllByTermo(@Param("termo") String termo);
}
