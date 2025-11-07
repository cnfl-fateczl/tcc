package com.gerencia_restaurante.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.gerencia_restaurante.application.port.in.CadastrarPedidoComanda;
import com.gerencia_restaurante.application.port.out.PedidoComandaItens;
import com.gerencia_restaurante.domain.entity.PedidoComanda;

@Mapper(componentModel = "spring", uses=com.gerencia_restaurante.application.mapper.ItemPedidoMapper.class)
public interface PedidoComandaMapper
{
    @Mapping(target="itens", source="itens")
    @Mapping(target="id", ignore=true)
    PedidoComanda toPedidoComanda(CadastrarPedidoComanda dto);
    
    @Mapping(target="itens", source="itens")
    PedidoComandaItens toPedidoComandaItens(PedidoComanda pedido);
}