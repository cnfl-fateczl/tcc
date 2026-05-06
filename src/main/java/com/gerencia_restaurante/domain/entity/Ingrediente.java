package com.gerencia_restaurante.domain.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Ingrediente
{
    @EmbeddedId
    private IngredienteId id; 

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("produtoId")
    @JoinColumn(name="produto_id")
    private Produto produto;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("itemdeEstoqueId")
    @JoinColumn(name="item_de_estoque_id")
    private ItemdeEstoque itemdeEstoque;

    private Integer quantidade;
}
