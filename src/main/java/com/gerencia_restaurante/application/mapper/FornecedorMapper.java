package com.gerencia_restaurante.application.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.gerencia_restaurante.application.port.in.CadastrarFornecedor;
import com.gerencia_restaurante.domain.entity.Fornecedor;

@Mapper(componentModel="spring")
public interface FornecedorMapper
{
    @Mapping(target="id", ignore=true)
    Fornecedor toEnttityFromCadastrar(CadastrarFornecedor dto);

    @Mapping(target="id", ignore=true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromCadastrar(CadastrarFornecedor dto, @MappingTarget Fornecedor fornecedor);
}
