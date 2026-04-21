package com.gerencia_restaurante.application.port.in;

import jakarta.validation.constraints.NotBlank;

public record CadastrarItemdeEstoque (
    @NotBlank(message = "O nome do Item é obrigatório")
    String nome,

    @NotBlank(message = "Unidade do Item é obrigatório")
    Integer unidade
){}