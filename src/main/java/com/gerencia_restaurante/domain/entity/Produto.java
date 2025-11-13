package com.gerencia_restaurante.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gerencia_restaurante.application.port.in.AtualizarProduto;
import com.gerencia_restaurante.application.port.in.CadastrarProduto;
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

    @ManyToMany(mappedBy = "produtos")
    @Builder.Default
    @JsonIgnore
    private Set<Cardapio> cardapios = new HashSet<>();

    public Produto(CadastrarProduto dados){
        this.nome = dados.nome();
        this.categoria = dados.categoria();
        this.descricao = dados.descricao();
        this.precoProduto = dados.precoProduto();
    }

    public Produto(AtualizarProduto dados){
        this.nome = dados.nome();
        this.categoria = dados.categoria();
        this.descricao = dados.descricao();
        this.precoProduto = dados.precoProduto();
    }

}
