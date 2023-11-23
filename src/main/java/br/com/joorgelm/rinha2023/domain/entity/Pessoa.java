package br.com.joorgelm.rinha2023.domain.entity;


import br.com.joorgelm.rinha2023.domain.converter.PessoaStackConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Entity(name = "Pessoa")
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ]+(?:[-'` ]?[A-Za-zÀ-ÖØ-öø-ÿ]+)*$")
    @Size(max = 32)
    @Column(unique = true)
    private String apelido;

    @NotBlank
    @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ]+(?:[-'` ]?[A-Za-zÀ-ÖØ-öø-ÿ]+)*$")
    @Size(max = 32)
    private String nome;

    @NotBlank
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

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Pessoa>> constraintViolations = validator.validate(this);

        if (constraintViolations.isEmpty()) return;

        throw new HttpClientErrorException(HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
