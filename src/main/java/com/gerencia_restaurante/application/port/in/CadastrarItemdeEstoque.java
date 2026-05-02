package com.gerencia_restaurante.application.port.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

public record CadastrarItemdeEstoque (
    @NotBlank(message = "O nome do Item é obrigatório")
    String nome,

    @NotBlank(message = "Unidade do Item é obrigatório")
    String unidade,

    @NotNull(message = "Quantidade do Item é obrigatório. Quantidade mínima é zero.")
    @Min(0)
    Integer quantidade
){}