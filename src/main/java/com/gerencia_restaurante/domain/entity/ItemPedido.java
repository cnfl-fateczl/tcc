package com.gerencia_restaurante.domain.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="item_pedido")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of="id")
public class ItemPedido
{
    @EmbeddedId
    private ItemPedidoId id;

    @ManyToOne(fetch=FetchType.LAZY)
    @MapsId("pedidoId")
    @JoinColumn(name="pedido_id")
    private PedidoComanda pedido;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="produto_id")
    private Produto produto;

    private Integer quantidade;
    private String observacao;
}
