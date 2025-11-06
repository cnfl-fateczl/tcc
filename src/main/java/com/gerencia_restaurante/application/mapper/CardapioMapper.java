package com.gerencia_restaurante.application.mapper;

import com.gerencia_restaurante.application.port.in.AtualizarCardapio;
import com.gerencia_restaurante.application.port.in.CadastrarCardapio;
import com.gerencia_restaurante.domain.entity.Cardapio;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CardapioMapper {

    // Converte Entity para DTO
    AtualizarCardapio toAtualizarCardapio(Cardapio cardapio);

    // Converte DTO para Entity (criação nova)
    @Mapping(target = "id", ignore = true)
    Cardapio toCardapioFromCadastrarCardapio(CadastrarCardapio cadastrarCardapio);

    // Atualiza Entity existente com dados do DTO
    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCardapioFromDto(AtualizarCardapio atualizarCardapio, @MappingTarget Cardapio cardapio);
}
