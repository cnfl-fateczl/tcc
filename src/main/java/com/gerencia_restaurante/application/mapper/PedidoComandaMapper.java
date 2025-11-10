package com.gerencia_restaurante.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.gerencia_restaurante.application.port.in.CadastrarPedidoComanda;
import com.gerencia_restaurante.application.port.out.PedidoComandaItens;
import com.gerencia_restaurante.application.port.out.PedidoComandaResumo;
import com.gerencia_restaurante.domain.entity.PedidoComanda;

@Mapper(componentModel = "spring", uses=com.gerencia_restaurante.application.mapper.ItemPedidoMapper.class)
public interface PedidoComandaMapper
{
    @Mapping(target="itens", source="itens")
    @Mapping(target="data", ignore=true)
    @Mapping(target="id", ignore=true)
    PedidoComanda toPedidoComanda(CadastrarPedidoComanda dto);
    
    @Mapping(target="itens", source="itens")
    PedidoComandaItens toPedidoComandaItens(PedidoComanda pedido);

    default PedidoComandaResumo toPedidoComandaResumo(PedidoComanda pedido)
    {
        Integer qtdItens = pedido.getItens().size();
        Double valorTotal = pedido.getItens().stream()
            .map(it -> it.getQuantidade() * it.getProduto().getPrecoProduto())
            .reduce(Double::sum).orElse(0D);
        return PedidoComandaResumo.builder()
            .id(pedido.getId())
            .data(pedido.getData())
            .mesa(pedido.getMesa())
            .qtdItens(qtdItens)
            .valorTotal(valorTotal)
            .build();
    }
}