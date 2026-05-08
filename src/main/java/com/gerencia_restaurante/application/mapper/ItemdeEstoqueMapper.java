package com.gerencia_restaurante.application.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.gerencia_restaurante.application.port.in.AtualizarItemdeEstoque;
import com.gerencia_restaurante.application.port.in.CadastrarItemdeEstoque;
import com.gerencia_restaurante.domain.entity.ItemdeEstoque;

@Mapper(componentModel = "spring")
public interface ItemdeEstoqueMapper {

    //DTO para Entity
    @Mapping(target = "id", ignore = true)
    ItemdeEstoque toEntityFromCadastrar(CadastrarItemdeEstoque cadastrarItemdeEstoque);

    //DTO para Entity com Patch
    @Mapping(target="id", ignore=true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromAtualizar(AtualizarItemdeEstoque dto, @MappingTarget ItemdeEstoque itemdeEstoque);
}