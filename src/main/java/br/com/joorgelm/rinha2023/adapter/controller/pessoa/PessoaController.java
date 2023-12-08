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

    @PostMapping("/pessoas")
    public ResponseEntity<Void> cadastrar(@RequestBody Pessoa pessoa) {
        String pessoaUUID = pessoaService.cadastrarPessoa(pessoa);
        return ResponseEntity.created(URI.create("/pessoas/" + pessoaUUID)).build();
    }

    @GetMapping("/pessoas/{pessoaId}")
    public ResponseEntity<Pessoa> buscaPorId(@PathVariable("pessoaId") UUID pessoaId, @RequestParam(defaultValue = "false") boolean sibling) {
        return new ResponseEntity<>(pessoaService.buscarPorId(pessoaId, sibling), HttpStatus.OK);
    }


    @GetMapping("/pessoas/apelidos/{apelido}")
    public ResponseEntity<Void> buscaPorApelido(@PathVariable("apelido") String apelido, @RequestParam(defaultValue = "false") boolean sibling) {
        if (pessoaService.buscarPorApelido(apelido, sibling)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/pessoas")
    public ResponseEntity<List<Pessoa>> buscaPorTermo(@RequestParam String t, @RequestParam(defaultValue = "false") boolean sibling) {
        return new ResponseEntity<>(pessoaService.buscaPorTermo(t, sibling), HttpStatus.OK);
    }

    @GetMapping("/contagem-pessoas")
    public ResponseEntity<Long> contagem(@RequestParam(defaultValue = "false") boolean sibling) {
        return new ResponseEntity<>(pessoaService.contagemPessoas(sibling), HttpStatus.OK);
    }
}
