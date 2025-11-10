package com.gerencia_restaurante.domain.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of={"pedidoId", "numeroItem"})
public class ItemPedidoId
{
    private Long pedidoId;
    private Integer numeroItem;
}
