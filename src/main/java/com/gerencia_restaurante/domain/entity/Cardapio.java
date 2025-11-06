package com.gerencia_restaurante.domain.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gerencia_restaurante.application.port.in.AtualizarCardapio;
import com.gerencia_restaurante.application.port.in.AtualizarProduto;
import com.gerencia_restaurante.application.port.in.CadastrarCardapio;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "cardapio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Cardapio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cardapio_id")
    private Long id;
    private String nome;
    private String descricao;
    private Boolean statusDisponivel = false;
    private List<Produto> produtos;

    public Cardapio(CadastrarCardapio dados) {
        this.nome = dados.nome();
        this.descricao = dados.descricao();
        this.statusDisponivel = dados.statusDisponivel();
        this.produtos = dados.produtos();
    }

    public Cardapio(AtualizarCardapio dados) {
        this.nome = dados.nome();
        this.descricao = dados.descricao();
        this.statusDisponivel = dados.statusDisponivel();
        this.produtos = dados.produtos();
    }
}
