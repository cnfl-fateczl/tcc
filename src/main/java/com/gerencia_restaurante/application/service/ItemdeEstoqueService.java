package com.gerencia_restaurante.application.service;

import java.util.List;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gerencia_restaurante.application.mapper.ItemdeEstoqueMapper;
import com.gerencia_restaurante.application.port.in.CadastrarItemdeEstoque;
import com.gerencia_restaurante.domain.entity.ItemdeEstoque;
import com.gerencia_restaurante.domain.repository.ItemdeEstoqueRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class ItemdeEstoqueService {

    @Autowired
    private ItemdeEstoqueRepository itemRepository;

    @Autowired
    private ItemdeEstoqueMapper itemMapper;

    //GET por id
    public ItemdeEstoque buscarPorId(Long id)
    {
        return itemRepository.findById(id).orElse(null);
    }

    //GET por nome
    public List<ItemdeEstoque> buscaPorNome(String nome) {
        return itemRepository.findByNome(nome);
    }

    //GET all
    public List<ItemdeEstoque> buscaTodos(){
        return itemRepository.findAll();
    }

    //POST
    @Transactional
    public ItemdeEstoque cadastrarItemdeEstoque(CadastrarItemdeEstoque dto)
    {
        ItemdeEstoque novo = itemMapper.toEntityFromCadastrar(dto);
        return itemRepository.save(novo);
    }

    //PUT
    @Transactional
    public ItemdeEstoque atualizarItemdeEstoqueTotal(CadastrarItemdeEstoque dto, Long id)
    {
        ItemdeEstoque existente = itemRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Item não encontrado com o Id: " + id));
        ItemdeEstoque atualizado = itemMapper.toEntityFromCadastrar(dto);
        atualizado.setId(id);
        return itemRepository.save(existente);
    }

    //PATCH
    @Transactional
    public ItemdeEstoque atualizarItemdeEstoqueParcial(CadastrarItemdeEstoque dto, Long id)
    {
        ItemdeEstoque existente = itemRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Item não encontrado com o Id: " + id));
        itemMapper.updateEntityFromCadastrar(dto, existente);
        return itemRepository.save(existente);
    }

    //DELETE
    @Transactional
    public void apagarPorId(Long id)
    {
        itemRepository.deleteById(id);
    }

}