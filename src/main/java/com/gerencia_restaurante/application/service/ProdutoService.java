package com.gerencia_restaurante.application.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gerencia_restaurante.application.mapper.ProdutoMapper;
import com.gerencia_restaurante.application.port.in.AtualizarProduto;
import com.gerencia_restaurante.application.port.in.CadastrarProduto;
import com.gerencia_restaurante.domain.entity.Produto;
import com.gerencia_restaurante.domain.repository.ProdutoRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ProdutoMapper produtoMapper;

    @Transactional
    public Produto atualizar(AtualizarProduto dto, Long id){
        Produto existente = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com ID: " + dto.id()));
        produtoMapper.updateProdutoFromDto(dto, existente);
        return produtoRepository.save(existente);
    }

    @Transactional
    public Produto salvar(CadastrarProduto dto){
        Produto novo = produtoMapper.toProdutoFromCadastrarProduto(dto);
        return produtoRepository.save(novo);
    }

    @Transactional
    public List<Produto> salvarLista(List<CadastrarProduto> listaDto)
    {
        List<Produto> produtos = listaDto.stream()
            .map(dto -> produtoMapper.toProdutoFromCadastrarProduto(dto))
            .toList();
        return produtoRepository.saveAll(produtos);
    }

    public List<Produto> filtrar(String nome, String categoria, Double precoMinimo, Double precoMaximo)
    {
        List<Predicate<Produto>> filtros = new ArrayList<>();

        if (nome != null)
            filtros.add(p -> p.getNome().toLowerCase().contains(nome.toLowerCase()));
        if (categoria != null)
            filtros.add(p -> p.getCategoria().equals(categoria));
        if (precoMinimo != null)
            filtros.add(p -> p.getPrecoProduto() >= precoMinimo);
        if (precoMaximo != null)
            filtros.add(p -> p.getPrecoProduto() <= precoMaximo);        
        Predicate<Produto> filtro = filtros.stream().reduce((p1, p2) -> p1.and(p2)).orElse(p -> true);
        
        return produtoRepository.findAll().stream().filter(filtro).toList();
    }

    public List<Produto> procurarTodos() {
        return produtoRepository.findAll();
    }

    public Optional<Produto> procurarPorId(Long id) {
        return produtoRepository.findById(id);
    }

    public List<Produto> procurarPorNome(String nome) {
        return produtoRepository.findByNome(nome);
    }

    @Transactional
    public void apagarPorId(Long id) {
        produtoRepository.deleteById(id);
    }
}
