package com.gerencia_restaurante.adapters.web;

import com.gerencia_restaurante.application.port.in.AtualizarCardapio;
import com.gerencia_restaurante.application.port.in.AtualizarProduto;
import com.gerencia_restaurante.application.port.in.CadastrarCardapio;
import com.gerencia_restaurante.application.service.CardapioService;
import com.gerencia_restaurante.domain.entity.Cardapio;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cardapio")
@CrossOrigin("*")
public class CardapioController {

    @Autowired
    private CardapioService cardapioService;

    @GetMapping
    public List<Cardapio> listarComFiltros(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String descricao,
            @RequestParam(required = false) Boolean statusDisponivel
    ){
        return cardapioService.filtrar(nome, descricao, statusDisponivel);
    }

    @GetMapping("/{id}")
    public Optional<Cardapio> listarUmCardapio(@PathVariable Long id){
        return cardapioService.procuraPorId(id);
    }

    @PostMapping
    @Transactional
    public void cadastrarCardapio(@RequestBody @Valid CadastrarCardapio novoCardapio){
        cardapioService.salvar(novoCardapio);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void excluirCardapio(@PathVariable Long id){
        cardapioService.apagarPorId(id);
    }

    @PutMapping("/{id}")
    @Transactional
    public void atualizarCardapio(@RequestBody @Valid AtualizarCardapio novoCardapio, @PathVariable Long id) {
       cardapioService.atualizar(novoCardapio, id);
    }
}

