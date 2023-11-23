package br.com.joorgelm.rinha2023.adapter.controller.pessoa;

import br.com.joorgelm.rinha2023.application.service.PessoaService;
import br.com.joorgelm.rinha2023.domain.entity.Pessoa;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping
public class PessoaController {

    private final PessoaService pessoaService;

    public PessoaController(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    /*
    * Para requisições válidas, sua API deverá retornar status code 201 - created junto com o header "Location: /pessoas/[:id]"
    * onde [:id] é o id – em formato UUID com a versão a seu critério –
    * da pessoa que acabou de ser criada. O conteúdo do corpo fica a seu critério; retorne o que quiser.
    * */

    @PostMapping("/pessoas")
    public ResponseEntity<Void> cadastrar(@RequestBody Pessoa pessoa) {
        Pessoa pessoaCadastrada = pessoaService.cadastrarPessoa(pessoa);
        return ResponseEntity.created(URI.create("/pessoas/" + pessoaCadastrada.getId())).build();
    }

    @GetMapping("/pessoas/{pessoaId}")
    public ResponseEntity<Pessoa> buscaPorId(@PathVariable("pessoaId") UUID pessoaId) {
        return new ResponseEntity<>(pessoaService.buscarPorId(pessoaId), HttpStatus.OK);
    }

    @GetMapping("/pessoas")
    public ResponseEntity<List<Pessoa>> buscaPorTermo(@RequestParam String t) {
        return new ResponseEntity<>(pessoaService.buscaPorTermo(t), HttpStatus.OK);
    }

    @GetMapping("/contagem-pessoas")
    public ResponseEntity<Long> contagem() {
        return new ResponseEntity<>(pessoaService.contagemPessoas(), HttpStatus.OK);
    }
}
