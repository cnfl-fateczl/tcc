package com.gerencia_restaurante.application.service;

import com.gerencia_restaurante.application.mapper.CardapioMapper;
import com.gerencia_restaurante.application.port.in.AtualizarCardapio;
import com.gerencia_restaurante.application.port.in.CadastrarCardapio;
import com.gerencia_restaurante.domain.entity.Cardapio;
import com.gerencia_restaurante.domain.entity.Produto;
import com.gerencia_restaurante.domain.repository.CardapioRepository;
import com.gerencia_restaurante.domain.repository.ProdutoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Predicate;

@Service
public class CardapioService {
    @Autowired
    private CardapioRepository cardapioRepository;

    @Autowired
    private CardapioMapper cardapioMapper;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Transactional
    public Cardapio salvar(CadastrarCardapio dto) {
        Cardapio novo = cardapioMapper.toEntityFromCadastrar(dto);

        if (dto.produtosIds() != null && !dto.produtosIds().isEmpty()) {
            Set<Produto> produtos = new HashSet<>(produtoRepository.findAllById(dto.produtosIds()));
            novo.setProdutos(produtos);
        }

        return cardapioRepository.save(novo);
    }


    @Transactional
    public Cardapio atualizar(AtualizarCardapio dto, Long id) {
        Cardapio existente = cardapioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cardapio não encontrado com ID: " + dto.id()));
        cardapioMapper.updateEntityFromAtualizar(dto, existente);
        if(dto.produtosIds() != null){
            Set<Produto> produtos = new HashSet<>(produtoRepository.findAllById(dto.produtosIds()));
            existente.setProdutos(produtos);
        }
        return cardapioRepository.save(existente);
    }

    public List<Cardapio> filtrar(String nome, String descricao, Boolean statusDisponivel){
        List<Predicate<Cardapio>> filtros = new ArrayList<>();

        if (nome != null)
            filtros.add((cardapio -> cardapio.getNome().toLowerCase().contains(nome.toLowerCase())));
        if (descricao != null)
            filtros.add((cardapio -> cardapio.getDescricao().toLowerCase().contains(descricao.toLowerCase())));
        if (statusDisponivel != null)
            filtros.add((cardapio -> cardapio.getStatusDisponivel() == statusDisponivel));
        Predicate<Cardapio> filtro = filtros.stream().reduce(Predicate::and).orElse(cardapio -> true);
        return cardapioRepository.findAll().stream().filter(filtro).toList();
    }

    public List<Cardapio> procuraTodos(){
        return cardapioRepository.findAll();
    }

    public Optional<Cardapio> procuraPorId(Long id) {
        return cardapioRepository.findById(id);
    }

    public List<Cardapio> procurarPorNome(String nome){
        return cardapioRepository.findByNome(nome);
    }

    @Transactional
    public void apagarPorId(Long id){
        cardapioRepository.deleteById(id);
    }
}
