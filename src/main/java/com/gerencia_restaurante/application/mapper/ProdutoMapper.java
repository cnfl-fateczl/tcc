package com.gerencia_restaurante.application.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.gerencia_restaurante.application.port.in.AtualizarProduto;
import com.gerencia_restaurante.application.port.in.CadastrarProduto;
import com.gerencia_restaurante.application.port.in.IngredienteDto;
import com.gerencia_restaurante.application.port.out.IngredienteSaida;
import com.gerencia_restaurante.application.port.out.ProdutoSaida;
import com.gerencia_restaurante.domain.entity.Ingrediente;
import com.gerencia_restaurante.domain.entity.Produto;

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

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "itemdeEstoque", ignore = true)
    @Mapping(target = "produto", ignore = true)
    Ingrediente toIngredienteFromDto(IngredienteDto dto);

    @Mapping(target = "nome", source = "itemdeEstoque.nome")
    IngredienteSaida toIngredienteSaida(Ingrediente ingrediente);

    ProdutoSaida toProdutoSaida(Produto produto);
}
