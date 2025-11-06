package com.gerencia_restaurante.application.service;

import com.gerencia_restaurante.application.mapper.CardapioMapper;
import com.gerencia_restaurante.application.mapper.ProdutoMapper;
import com.gerencia_restaurante.application.port.in.AtualizarCardapio;
import com.gerencia_restaurante.application.port.in.CadastrarCardapio;
import com.gerencia_restaurante.domain.entity.Cardapio;
import com.gerencia_restaurante.domain.entity.Produto;
import com.gerencia_restaurante.domain.repository.CardapioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service
public class CardapioService {
    @Autowired
    private CardapioRepository cardapioRepository;

    @Autowired
    private CardapioMapper cardapioMapper;
    @Autowired
    private ProdutoMapper produtoMapper;

    @Transactional
    public Cardapio atualizar(AtualizarCardapio dto, Long id) {
        Cardapio existente = cardapioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cardapio não encontrado com ID: " + dto.id()));
        cardapioMapper.updateCardapioFromDto(dto, existente);
        return cardapioRepository.save(existente);
    }

    @Transactional
    public Cardapio salvar(CadastrarCardapio dto){
        Cardapio novo = cardapioMapper.toCardapioFromCadastrarCardapio(dto);
        return cardapioRepository.save(novo);
    }

    @Transactional
    public List<Cardapio> salvarLista(List<CadastrarCardapio> listaDto){
        List<Cardapio> cardapios = listaDto.stream()
                .map(dto -> cardapioMapper.toCardapioFromCadastrarCardapio(dto))
                .toList();
        return cardapioRepository.saveAll(cardapios);
    }

    public List<Cardapio> filtrar(String nome, String descricao, Boolean statusDisponivel){
        List<Predicate<Cardapio>> filtros = new ArrayList<>();

        if (nome != null)
            filtros.add((cardapio -> cardapio.getNome().contains(nome.toLowerCase())));
        if (descricao != null)
            filtros.add((cardapio -> cardapio.getDescricao().contains(descricao.toLowerCase())));
        if (statusDisponivel != null)
            filtros.add((cardapio -> cardapio.getStatusDisponivel() == statusDisponivel));
        Predicate<Cardapio> filtro = filtros.stream().reduce((cardapio1, cardapio2) -> cardapio1.and(cardapio2)).orElse(cardapio -> true);
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
