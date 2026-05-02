package com.gerencia_restaurante.application.mapper;

import com.gerencia_restaurante.application.port.in.AtualizarProduto;
import com.gerencia_restaurante.application.port.in.CadastrarProduto;
import com.gerencia_restaurante.domain.entity.Produto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {

    // Converte Entity para DTO
    AtualizarProduto toAtualizarProduto(Produto produto);

    // Converte DTO para Entity (criação nova)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cardapios", ignore = true)
    Produto toProdutoFromCadastrarProduto(CadastrarProduto cadastrarProduto);

    // Atualiza Entity existente com dados do DTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cardapios", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProdutoFromDto(AtualizarProduto atualizarProduto, @MappingTarget Produto produto);
}
