package com.gerencia_restaurante.domain.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="pedido_comanda")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of="id")
public class PedidoComanda
{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="pedido_id")
    private Long id;

    @OneToMany(mappedBy="pedido", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<ItemPedido> itens = new ArrayList<>();

    public void adicionarItem(ItemPedido item)
    {
        if (itens == null)
            itens = new ArrayList<>();
        item.setPedido(this);
        if (item.getId() == null) {
            ItemPedidoId id = new ItemPedidoId();
            item.setId(id);
        }
        item.getId().setPedidoId(this.id);
        item.getId().setNumeroItem(this.itens.size() + 1);
        itens.add(item);
    }
}
