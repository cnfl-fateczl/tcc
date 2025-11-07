package com.gerencia_restaurante.application.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gerencia_restaurante.application.mapper.ItemPedidoMapper;
import com.gerencia_restaurante.application.mapper.PedidoComandaMapper;
import com.gerencia_restaurante.application.port.in.CadastrarPedidoComanda;
import com.gerencia_restaurante.application.port.out.PedidoComandaItens;
import com.gerencia_restaurante.application.port.out.PedidoComandaResumo;
import com.gerencia_restaurante.domain.entity.ItemPedido;
import com.gerencia_restaurante.domain.entity.ItemPedidoId;
import com.gerencia_restaurante.domain.entity.PedidoComanda;
import com.gerencia_restaurante.domain.entity.Produto;
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
    
    @Transactional
    public PedidoComandaItens salvar(CadastrarPedidoComanda dto)
    {
        // 1o passo -> Salvar pedido vazio para pegar id
        PedidoComanda pedido = new PedidoComanda();
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
        // 3o passo -> terminar de preencher atributos de item
        Integer contador = 1;
        for (ItemPedido item : itens) {
            ItemPedidoId id = new ItemPedidoId(pedido.getId(), contador++);
            item.setId(id);
            item.setPedido(pedido);
        }
        // 4o passo -> adicionar itens e salvar novamente o pedido
        pedido.setItens(itens);
        pedidoComandaRepository.save(pedido);
        return pedidoComandaMapper.toPedidoComandaItens(pedido);
    }

    public List<PedidoComandaResumo> procurarTodos()
    {
        List<PedidoComanda> pedidos = pedidoComandaRepository.findAll();
        List<PedidoComandaResumo> resumos = new ArrayList<>();

        for (PedidoComanda pedido : pedidos) {
            Integer qtdItens = pedido.getItens().size();
            Double valorTotal = pedido.getItens().stream()
                .map(it -> it.getQuantidade() * it.getProduto().getPrecoProduto())
                .reduce(Double::sum).orElse(0D);
            PedidoComandaResumo resumo = new PedidoComandaResumo(pedido.getId(), qtdItens, valorTotal);
            resumos.add(resumo);
        }
        return resumos;
    }

    public PedidoComandaItens procurarPorId(Long id)
    {
        PedidoComanda pedido = pedidoComandaRepository.findById(id).orElse(null);
        if (pedido == null)
            return null;
        return pedidoComandaMapper.toPedidoComandaItens(pedido);
    }
}