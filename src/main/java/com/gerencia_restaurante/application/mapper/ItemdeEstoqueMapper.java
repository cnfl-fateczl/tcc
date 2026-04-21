package com.gerencia_restaurante.application.mapper;

import com.gerencia_restaurante.application.port.in.CadastrarItemdeEstoque;
import com.gerencia_restaurante.domain.entity.ItemdeEstoque;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ItemdeEstoqueMapper {

    //DTO para Entity
    @Mapping(target = "id", ignore = true)
    ItemdeEstoque toEntityfromCadastrar(CadastrarItemdeEstoque cadastrarItemdeEstoque);

}