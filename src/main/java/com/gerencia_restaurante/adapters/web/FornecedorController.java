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

import com.gerencia_restaurante.application.port.in.CadastrarFornecedor;
import com.gerencia_restaurante.application.service.FornecedorService;
import com.gerencia_restaurante.domain.entity.Fornecedor;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/fornecedor")
@CrossOrigin("*")
public class FornecedorController
{
    @Autowired
    private FornecedorService fornecedorService;

    @GetMapping
    public List<Fornecedor> listarComFiltros(
        @RequestParam(required = false) String razaosocial,
        @RequestParam(required = false) String cnpj,
        @RequestParam(required = false) String endereco,
        @RequestParam(required = false) String telefone,
        @RequestParam(required = false) String email)
    {
        return fornecedorService.listarComFiltros(
            razaosocial, cnpj, endereco, telefone, email);
    }

    @GetMapping("/{id}")
    public Fornecedor bucarPorId(@PathVariable("id") Long id)
    {
        return fornecedorService.buscarPorId(id);
    }

    @PostMapping
    public Fornecedor cadastrarFornecedor(
        @RequestBody @Valid CadastrarFornecedor dto)
    {
        return fornecedorService.cadastrarFornecedor(dto);
    }

    @PutMapping("/{id}")
    public Fornecedor atualizarFornecedorTotal(
        @RequestBody @Valid CadastrarFornecedor dto,
        @PathVariable("id") Long id)
    {
        return fornecedorService.atualizarFornecedorTotal(dto, id);
    }

    @PatchMapping("/{id}")
    public Fornecedor atualizarFornecedorParcial(
        @RequestBody @Valid CadastrarFornecedor dto,
        @PathVariable("id") Long id)
    {
        return fornecedorService.atualizarFornecedorParcial(dto, id);
    }

    @DeleteMapping("{id}")
    public void deletarPorId(@PathVariable("id") Long id)
    {
        fornecedorService.apagarPorId(id);
    }
}
