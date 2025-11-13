package com.gerencia_restaurante.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="fornecedor")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of="id")
public class Fornecedor
{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String razaoSocial;
    private String cnpj;
    private String email;
    private String telefone;
    private String endereco;
}
