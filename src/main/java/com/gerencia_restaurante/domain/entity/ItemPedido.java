package com.gerencia_restaurante.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "item_pedido")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// VERIRICAR
@EqualsAndHashCode(of = "id")
public class ItemPedido
{
    //Relacao oneToMany ou manyToOne?
    private PedidoComanda pedido;
    private Long numeroItem;
    //Relacao oneToMany ou manyToOne?
    private Produto produto;
    private int quantidade;
    private String descricao;
}
