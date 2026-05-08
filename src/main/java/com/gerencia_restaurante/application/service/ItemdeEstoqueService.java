package com.gerencia_restaurante.application.service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gerencia_restaurante.application.mapper.ItemdeEstoqueMapper;
import com.gerencia_restaurante.application.port.in.AtualizarItemdeEstoque;
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
    public List<ItemdeEstoque> filtrar(String nome)
    {
        List<Predicate<ItemdeEstoque>> filtros = new ArrayList<>();

        if (nome != null)
            filtros.add(p -> p.getNome().toLowerCase().contains(nome.toLowerCase()));     
        Predicate<ItemdeEstoque> filtro = filtros.stream().reduce((p1, p2) -> p1.and(p2)).orElse(p -> true);
        
        return itemRepository.findAll().stream().filter(filtro).toList();
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
        if (!itemRepository.existsById(id)){
            throw new EntityNotFoundException("Item não existe com o ID: " + id);
        }
        ItemdeEstoque existente = itemMapper.toEntityFromCadastrar(dto);
        existente.setId(id);
        return itemRepository.save(existente);
    }

    //PATCH
    @Transactional
    public ItemdeEstoque atualizarItemdeEstoqueParcial(AtualizarItemdeEstoque dto, Long id)
    {
        ItemdeEstoque existente = itemRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Item não encontrado com o Id: " + id));
        itemMapper.updateEntityFromAtualizar(dto, existente);
        return itemRepository.save(existente);
    }

    //DELETE
    @Transactional
    public void apagarPorId(Long id)
    {
        if (!itemRepository.existsById(id)){
            throw new EntityNotFoundException("ID não existe");
        }
        itemRepository.deleteById(id);
    }

}