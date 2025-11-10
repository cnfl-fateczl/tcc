package com.gerencia_restaurante.application.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gerencia_restaurante.application.mapper.ItemPedidoMapper;
import com.gerencia_restaurante.application.mapper.PedidoComandaMapper;
import com.gerencia_restaurante.application.port.in.CadastrarItemPedido;
import com.gerencia_restaurante.application.port.in.CadastrarPedidoComanda;
import com.gerencia_restaurante.application.port.out.ItemPedidoSaida;
import com.gerencia_restaurante.application.port.out.PedidoComandaItens;
import com.gerencia_restaurante.application.port.out.PedidoComandaResumo;
import com.gerencia_restaurante.domain.entity.ItemPedido;
import com.gerencia_restaurante.domain.entity.ItemPedidoId;
import com.gerencia_restaurante.domain.entity.PedidoComanda;
import com.gerencia_restaurante.domain.entity.Produto;
import com.gerencia_restaurante.domain.repository.ItemPedidoRepository;
import com.gerencia_restaurante.domain.repository.PedidoComandaRepository;
import com.gerencia_restaurante.domain.repository.ProdutoRepository;

import jakarta.transaction.Transactional;

@Service
public class PedidoComandaService
{
    @Autowired
    private PedidoComandaRepository pedidoComandaRepository;

    @Autowired
    private PedidoComandaMapper pedidoComandaMapper;

    @Autowired
    private ItemPedidoMapper itemPedidoMapper;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;
    
    @Transactional
    public PedidoComandaItens salvar(CadastrarPedidoComanda dto)
    {
        // 1o passo -> Salvar pedido vazio para pegar id
        PedidoComanda pedido = new PedidoComanda();
        pedido.setData(LocalDate.now());
        pedido.setMesa(dto.mesa());
        pedido = pedidoComandaRepository.save(pedido);
        // 2o passo -> parsear lista de itens
        List<ItemPedido> itens = dto.itens().stream()
            .map(d -> {
                ItemPedido it = itemPedidoMapper.toItemPedido(d);
                Produto produto = produtoRepository.findById(d.produtoId()).orElse(null);
                it.setProduto(produto);
                return it;
            })
            .collect(Collectors.toList());
        // 3º passo -> adicionar itens no pedido
        for (ItemPedido item : itens)
            pedido.adicionarItem(item);
        // 4o passo -> persistir o pedido com os itens
        pedidoComandaRepository.save(pedido);
        return pedidoComandaMapper.toPedidoComandaItens(pedido);
    }

    public List<PedidoComandaResumo> procurarTodos()
    {
        List<PedidoComanda> pedidos = pedidoComandaRepository.findAll();
        List<PedidoComandaResumo> resumos = new ArrayList<>();

        for (PedidoComanda pedido : pedidos) {
            PedidoComandaResumo resumo = pedidoComandaMapper.toPedidoComandaResumo(pedido);
            resumos.add(resumo);
        }
        return resumos;
    }

    public void apagarPorId(Long id)
    {
        pedidoComandaRepository.deleteById(id);
    }

    public PedidoComandaResumo procurarPorId(Long id)
    {
        PedidoComanda pedido = pedidoComandaRepository.findById(id).orElse(null);
        if (pedido == null)
            return null;
        return pedidoComandaMapper.toPedidoComandaResumo(pedido);
    }

    public PedidoComandaItens listarItensPedido(Long id)
    {
        PedidoComanda pedido = pedidoComandaRepository.findById(id).orElse(null);
        if (pedido == null)
            return null;
        return pedidoComandaMapper.toPedidoComandaItens(pedido);
    }

    public ItemPedidoSaida adicionarItem(Long id, CadastrarItemPedido dto)
    {
        PedidoComanda pedido = pedidoComandaRepository.findById(id).orElse(null);        
        if (pedido == null)
            return null;
        Produto produto = produtoRepository.findById(dto.produtoId()).orElse(null);
        if (produto == null)
            return null;
        ItemPedido item = itemPedidoMapper.toItemPedido(dto);
        item.setProduto(produto);
        pedido.adicionarItem(item);
        pedidoComandaRepository.save(pedido);
        return itemPedidoMapper.toItemPedidoSaida(item);
    }

    public void removerItem(Long id, Integer numeroItem)
    {
        PedidoComanda pedido = pedidoComandaRepository.findById(id).orElse(null);
        if (pedido == null)
            return;
        ItemPedidoId itemId = new ItemPedidoId(pedido.getId(), numeroItem);
        ItemPedido item = itemPedidoRepository.findById(itemId).orElse(null);
        if (item == null)
            return;
        pedido.getItens().remove(item);
        pedidoComandaRepository.save(pedido);
    }
}