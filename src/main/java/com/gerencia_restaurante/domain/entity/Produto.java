package com.gerencia_restaurante.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gerencia_restaurante.application.port.in.AtualizarProduto;
import com.gerencia_restaurante.application.port.in.CadastrarProduto;
import com.gerencia_restaurante.domain.delivery.DeliveryItem;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "produto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "produto_id")
    private Long id;
    private String nome;
    private String categoria;
    private String descricao;
    private Double precoProduto;
    @Column(name = "codigo_ifood")
    private String codigoIfood;

    @ManyToMany(mappedBy = "produtos")
    @Builder.Default
    @JsonIgnore
    private Set<Cardapio> cardapios = new HashSet<>();

    @OneToMany(mappedBy = "produto")
    @JsonIgnore
    private Set<DeliveryItem> deliveryItems = new HashSet<>();
}
