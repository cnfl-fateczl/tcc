package com.gerencia_restaurante.adapters.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gerencia_restaurante.application.port.in.AtualizarItemdeEstoque;
import com.gerencia_restaurante.application.port.in.CadastrarItemdeEstoque;
import com.gerencia_restaurante.application.service.ItemdeEstoqueService;
import com.gerencia_restaurante.domain.entity.ItemdeEstoque;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/itemdeestoque")
@CrossOrigin("*")
public class ItemdeEstoqueController {

    @Autowired
    private ItemdeEstoqueService itemService;

    @GetMapping
    public List<ItemdeEstoque> listarComFiltros(@RequestParam(required=false) String nome) {
        return itemService.filtrar(nome);
    }

    @GetMapping("/{id}")
    public ItemdeEstoque listarPorId(@PathVariable Long id){
        return itemService.buscarPorId(id);
    }

    @PostMapping
    public void cadastrarItemdeEstoque(@RequestBody @Valid CadastrarItemdeEstoque novoItemdeEstoque){
        itemService.cadastrarItemdeEstoque(novoItemdeEstoque);
    }

    @DeleteMapping("/{id}")
    public void excluirItemdeEstoque(@PathVariable Long id){
        itemService.apagarPorId(id);
    }

    @PutMapping("/{id}")
    public void atualizaItemdeEstoqueTotal(@RequestBody @Valid CadastrarItemdeEstoque dto, @PathVariable Long id){
        itemService.atualizarItemdeEstoqueTotal(dto, id);
    }

    @PatchMapping("/{id}")
    public void atualizaItemdeEstoqueParcial(@RequestBody AtualizarItemdeEstoque dto, @PathVariable Long id){
        itemService.atualizarItemdeEstoqueParcial(dto, id);
    }
}