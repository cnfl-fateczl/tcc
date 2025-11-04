package com.gerencia_restaurante.adapters.web;

import com.gerencia_restaurante.application.mapper.ProdutoMapper;
import com.gerencia_restaurante.application.port.in.AtualizarProduto;
import com.gerencia_restaurante.application.service.ProdutoService;
import com.gerencia_restaurante.domain.entity.Produto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
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
    public String listaProdutos(Model model) {
        model.addAttribute("listaProdutos", produtoService.procurarTodos());
        return "produto/listagem";
    }

    @GetMapping("/{id}")
    public Optional<Produto> listaUmProduto(@PathVariable Long id) {
        return produtoService.procurarPorId(id);
    }

    @GetMapping
    @RequestMapping("buscaNomeProduto")
    public List<Produto> buscarPorNome(@RequestParam("nome") String nome) {
        return produtoService.procurarPorNome(nome);
    }

    @PostMapping
    @Transactional
    public void cadastrarProduto(@RequestBody @Valid AtualizarProduto novoProduto) {
        produtoService.salvarOuAtualizar(novoProduto);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void excluirProduto(@PathVariable Long id) {
        produtoService.apagarPorId(id);
    }

    @PutMapping("/{id}")
    @Transactional
    public void atualizarProduto(@RequestBody @Valid AtualizarProduto produtoAtualizado, @PathVariable Long id) {
        produtoService.salvarOuAtualizar(produtoAtualizado);
    }

}

