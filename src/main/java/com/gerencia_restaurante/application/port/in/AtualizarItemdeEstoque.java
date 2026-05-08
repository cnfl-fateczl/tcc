
package com.gerencia_restaurante.application.port.in;

import jakarta.validation.constraints.Min;

public record AtualizarItemdeEstoque (

    String nome,
    String unidade,
    @Min(value = 0, message = "Quantidade deve ser zero ou maior")
    Integer quantidade

) {}
