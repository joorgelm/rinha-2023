package br.com.joorgelm.rinha2023.application.messaging;

import br.com.joorgelm.rinha2023.application.repository.PessoaCustomRepository;
import br.com.joorgelm.rinha2023.application.repository.PessoaCustomRepositoryImpl;
import br.com.joorgelm.rinha2023.domain.entity.Pessoa;
import com.google.gson.Gson;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

//@Service
public class PessoaConsumer {

    private final PessoaCustomRepository pessoaCustomRepository;

    public PessoaConsumer(EntityManager entityManager) {
        this.pessoaCustomRepository = new PessoaCustomRepositoryImpl(entityManager);
    }

    @Transactional
    @KafkaListener(topics = "pessoa-topic")
    public void receiveMessage(String message) {

        Pessoa pessoa = new Gson().fromJson(message, Pessoa.class);
        pessoaCustomRepository.customSave(pessoa);
    }
}
