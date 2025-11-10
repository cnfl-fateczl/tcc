package com.gerencia_restaurante.application.mapper;

import com.gerencia_restaurante.application.port.in.AtualizarCardapio;
import com.gerencia_restaurante.application.port.in.CadastrarCardapio;
import com.gerencia_restaurante.domain.entity.Cardapio;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CardapioMapper {

    // Converte DTO para Entity (criação nova)
    @Mapping(target = "id", ignore = true)
    Cardapio toEntityFromCadastrar(CadastrarCardapio cadastrarCardapio);

    // Atualiza Entity existente com dados do DTO
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromAtualizar(AtualizarCardapio dto, @MappingTarget Cardapio cardapio);
}
