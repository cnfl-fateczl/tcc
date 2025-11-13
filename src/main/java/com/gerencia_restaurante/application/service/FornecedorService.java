package com.gerencia_restaurante.application.service;

import java.util.List;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gerencia_restaurante.application.mapper.FornecedorMapper;
import com.gerencia_restaurante.application.port.in.CadastrarFornecedor;
import com.gerencia_restaurante.domain.entity.Fornecedor;
import com.gerencia_restaurante.domain.repository.FornecedorRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class FornecedorService
{
    @Autowired
    private FornecedorRepository fornecedorRepository;
    
    @Autowired
    private FornecedorMapper fornecedorMapper;

    //GET
    public List<Fornecedor> listarComFiltros(String razao, String cnpj, String endereco, String telefone, String email)
    {
        List<Fornecedor> todos = fornecedorRepository.findAll();
        Predicate<Fornecedor> filtro = f -> true;
        if (razao != null)
            filtro = filtro.and(f -> f.getRazaoSocial().toLowerCase().contains(razao.toLowerCase()));
        if (cnpj != null)
            filtro = filtro.and(f -> f.getCnpj().toLowerCase().contains(cnpj.toLowerCase()));
        if (endereco != null)
            filtro = filtro.and(f -> f.getEndereco().toLowerCase().contains(endereco.toLowerCase()));
        if (telefone != null)
            filtro = filtro.and(f -> f.getTelefone().toLowerCase().contains(telefone.toLowerCase()));
        if (email != null)
            filtro = filtro.and(f -> f.getEmail().toLowerCase().contains(email.toLowerCase()));
        return todos.stream().filter(filtro).toList(); 
    }

    public Fornecedor buscarPorId(Long id)
    {
        return fornecedorRepository.findById(id).orElse(null);
    }

    //POST
    @Transactional
    public Fornecedor cadastrarFornecedor(CadastrarFornecedor dto)
    {
        Fornecedor novo = fornecedorMapper.toEnttityFromCadastrar(dto);
        return fornecedorRepository.save(novo);
    }

    //PUT
    @Transactional
    public Fornecedor atualizarFornecedorTotal(CadastrarFornecedor dto, Long id)
    {
        Fornecedor existente = fornecedorRepository.findById(id).orElse(null);
        if (existente == null)
            throw new EntityNotFoundException("Fornecedor não encontrado com o id: " + id);
        Fornecedor atualizado = fornecedorMapper.toEnttityFromCadastrar(dto);
        atualizado.setId(id);
        return fornecedorRepository.save(atualizado);

    }

    //PATCH
    @Transactional
    public Fornecedor atualizarFornecedorParcial(CadastrarFornecedor dto, Long id)
    {
        Fornecedor existente = fornecedorRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Fornecedor não encontrado com Id: " + id));
        fornecedorMapper.updateEntityFromCadastrar(dto, existente);
        return fornecedorRepository.save(existente);
    }

    //DELETE
    @Transactional
    public void apagarPorId(Long id)
    {
        fornecedorRepository.deleteById(id);
    }
}