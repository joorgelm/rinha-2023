package br.com.joorgelm.rinha2023.domain.entity;


import br.com.joorgelm.rinha2023.domain.converter.PessoaStackConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;

@Entity(name = "Pessoa")
@Table(indexes = @Index(name = "busca_idx", columnList = "busca"))
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    private String apelido;

    private String nome;

    private String nascimento;

    @Convert(converter = PessoaStackConverter.class)
    private List<String> stack;

    @Column(name = "busca",columnDefinition = "tsvector")
    private String vetorDeBusca;

    public Pessoa(UUID id, String apelido, String nome, String nascimento, List<String> stack) {
        this.id = id;
        this.apelido = apelido;
        this.nome = nome;
        this.nascimento = nascimento;
        this.stack = stack;
    }

    public Pessoa() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<String> getStack() {
        return stack;
    }

    public void setStack(List<String> stack) {
        this.stack = stack;
    }

    public String getNascimento() {
        return nascimento;
    }

    public void setNascimento(String nascimento) {
        this.nascimento = nascimento;
    }

    public void validarDados() {

        Optional.of(this).orElseThrow(IllegalStateException::new);

        if (Optional.ofNullable(this.stack).isEmpty()) {
            this.setStack(Collections.emptyList());
        }

        if (this.stack.stream().anyMatch(PessoaStackConverter::isNumeric)) {
            throw new HttpClientErrorException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (Strings.isBlank(this.nascimento)) {
            throw new HttpClientErrorException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (Strings.isBlank(this.nome) || this.nome.length() > 32) {
            throw new HttpClientErrorException(HttpStatus.UNPROCESSABLE_ENTITY);
        } else {
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("^[A-Za-zÀ-ÖØ-öø-ÿ]+(?:[-'` ]?[A-Za-zÀ-ÖØ-öø-ÿ]+)*$");
            Matcher matcher = pattern.matcher(this.nome);
            if (!matcher.find()) throw new HttpClientErrorException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (Strings.isBlank(this.apelido) || this.apelido.length() > 32) {
            throw new HttpClientErrorException(HttpStatus.UNPROCESSABLE_ENTITY);
        } else {
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("^[A-Za-zÀ-ÖØ-öø-ÿ]+(?:[-'` ]?[A-Za-zÀ-ÖØ-öø-ÿ]+)*$");
            Matcher matcher = pattern.matcher(this.apelido);
            if (!matcher.find()) throw new HttpClientErrorException(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
