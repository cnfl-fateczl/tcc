package com.gerencia_restaurante.application.port.in;

public record CadastrarFornecedor
(
    String razaoSocial,
    String cnpj,
    String email,
    String telefone,
    String endereco
) {}