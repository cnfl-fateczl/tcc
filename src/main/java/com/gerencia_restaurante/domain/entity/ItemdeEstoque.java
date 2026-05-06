package com.gerencia_restaurante.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name="item_de_estoque")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class ItemdeEstoque
{
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "item_de_estoque_id")
    private Long id;
    private String nome;
    private String unidade;
    private Integer quantidade;
}