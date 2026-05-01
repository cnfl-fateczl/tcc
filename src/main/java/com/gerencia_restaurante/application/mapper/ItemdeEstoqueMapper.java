package com.gerencia_restaurante.application.mapper;

import com.gerencia_restaurante.application.port.in.CadastrarItemdeEstoque;
import com.gerencia_restaurante.domain.entity.ItemdeEstoque;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ItemdeEstoqueMapper {

    //DTO para Entity
    @Mapping(target = "id", ignore = true)
    ItemdeEstoque toEntityFromCadastrar(CadastrarItemdeEstoque cadastrarItemdeEstoque);

    //DTO para Entity com Patch
    @Mapping(target="id", ignore=true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromCadastrar(CadastrarItemdeEstoque dto, @MappingTarget ItemdeEstoque itemdeEstoque);
}