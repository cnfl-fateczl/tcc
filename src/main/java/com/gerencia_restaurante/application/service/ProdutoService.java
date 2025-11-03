package com.gerencia_restaurante.application.service;

import com.gerencia_restaurante.application.mapper.ProdutoMapper;
import com.gerencia_restaurante.application.port.in.AtualizarProduto;
import com.gerencia_restaurante.domain.entity.Produto;
import com.gerencia_restaurante.domain.repository.ProdutoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ProdutoMapper produtoMapper;

    @Transactional
    public Produto salvarOuAtualizar(AtualizarProduto dto){
        if (dto.id() == null){
            Produto existente = produtoRepository.findById(dto.id())
                    .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com ID: " + dto.id()));
            produtoMapper.updateProdutoFromDto(dto, existente);
            return produtoRepository.save(existente);
        } else {
            Produto novo = produtoMapper.toProdutoFromAtualizarProduto(dto);
            return produtoRepository.save(novo);
        }
    }

    public List<Produto> procurarTodos() {
        return produtoRepository.findAll();
    }

    public Optional<Produto> procurarPorId(Long id) {
        return produtoRepository.findById(id);
    }

    @Transactional
    public void apagarPorId(Long id) {
        produtoRepository.deleteById(id);
    }
}
