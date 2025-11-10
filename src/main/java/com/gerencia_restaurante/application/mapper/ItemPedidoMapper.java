package com.gerencia_restaurante.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.gerencia_restaurante.application.port.in.CadastrarItemPedido;
import com.gerencia_restaurante.application.port.out.ItemPedidoSaida;
import com.gerencia_restaurante.domain.entity.ItemPedido;

@Mapper(componentModel="spring")
public interface ItemPedidoMapper
{
    @Mapping(target="id", ignore=true)
    @Mapping(target="pedido", ignore=true)
    @Mapping(target="produto", ignore=true)
    ItemPedido toItemPedido(CadastrarItemPedido dto);

    @Mapping(target="numeroItem", source="id.numeroItem")
    @Mapping(target="idProduto", source="produto.id")
    @Mapping(target="nomeProduto", source="produto.nome")
    @Mapping(target="precoProduto", source="produto.precoProduto")
    ItemPedidoSaida toItemPedidoSaida(ItemPedido item);
}
