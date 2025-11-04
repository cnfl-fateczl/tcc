package com.gerencia_restaurante.adapters.web;

import com.gerencia_restaurante.application.mapper.ProdutoMapper;
import com.gerencia_restaurante.application.port.in.AtualizarProduto;
import com.gerencia_restaurante.application.port.in.CadastrarProduto;
import com.gerencia_restaurante.application.service.ProdutoService;
import com.gerencia_restaurante.domain.entity.Produto;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/produto")
@CrossOrigin("*")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private ProdutoMapper produtoMapper;

    @GetMapping
    @RequestMapping("/listagem")
    public List<Produto> listaProdutos() {
        return produtoService.procurarTodos();
    }

    @GetMapping("/{id}")
    public Optional<Produto> listaUmProduto(@PathVariable Long id) {
        return produtoService.procurarPorId(id);
    }

    @GetMapping
    @RequestMapping("/nome")
    public List<Produto> buscarPorNome(@RequestParam("nome") String nome) {
        return produtoService.procurarPorNome(nome);
    }

    @PostMapping
    @Transactional
    public void cadastrarProduto(@RequestBody @Valid CadastrarProduto novoProduto) {
        produtoService.salvar(novoProduto);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void excluirProduto(@PathVariable Long id) {
        produtoService.apagarPorId(id);
    }

    @PutMapping("/{id}")
    @Transactional
    public void atualizarProduto(@RequestBody @Valid AtualizarProduto produtoAtualizado, @PathVariable Long id) {
        produtoService.atualizar(produtoAtualizado);
    }

}

