package com.gerencia_restaurante.adapters.web;

import com.gerencia_restaurante.application.port.in.AtualizarProduto;
import com.gerencia_restaurante.application.port.in.CadastrarProduto;
import com.gerencia_restaurante.application.service.ProdutoService;
import com.gerencia_restaurante.domain.entity.Produto;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/produto")
@CrossOrigin("*")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    /*@GetMapping
    public List<Produto> listaProdutos() {
        return produtoService.procurarTodos();
    }*/

    @GetMapping
    public List<Produto> listaComFiltros(@RequestParam(required=false) String nome,
                                         @RequestParam(required=false) String categoria,
                                         @RequestParam(required=false) Double precoMinimo,
                                         @RequestParam(required=false) Double precoMaximo)
    {
        return produtoService.filtrar(nome, categoria, precoMinimo, precoMaximo);
    }

    @GetMapping("/{id}")
    public Optional<Produto> listaUmProduto(@PathVariable Long id) {
        return produtoService.procurarPorId(id);
    }

    /*@GetMapping
    @RequestMapping("/nome")
    public List<Produto> buscarPorNome(@RequestParam("nome") String nome) {
        return produtoService.procurarPorNome(nome);
    }*/

    @PostMapping
    @Transactional
    public void cadastrarProduto(@RequestBody @Valid CadastrarProduto novoProduto) {
        produtoService.salvar(novoProduto);
    }

    @PostMapping("/lista")
    @Transactional
    public void cadastrarListaProdutos(@RequestBody List<CadastrarProduto> produtos)
    {
        produtoService.salvarLista(produtos);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void excluirProduto(@PathVariable Long id) {
        produtoService.apagarPorId(id);
    }

    @PutMapping("/{id}")
    @Transactional
    public void atualizarProduto(@RequestBody @Valid AtualizarProduto produtoAtualizado, @PathVariable Long id) {
        produtoService.atualizar(produtoAtualizado, id);
    }
}