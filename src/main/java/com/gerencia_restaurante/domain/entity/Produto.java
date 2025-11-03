package com.gerencia_restaurante.domain.entity;

import com.gerencia_restaurante.application.port.in.AtualizarProduto;
import com.gerencia_restaurante.application.port.in.CadastrarProduto;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "produto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "produto_id")
    private Long id;
    private String nome;
    private String categoria;
    private String descricao;
    private BigDecimal precoProduto;

    public Produto(CadastrarProduto dados){
        this.nome = dados.nome();
        this.categoria = dados.categoria();
        this.descricao = dados.descricao();
        this.precoProduto = dados.precoProduto();
    }

    public void atualizarProduto(AtualizarProduto dados){
        if (dados.nome() == null)
            this.nome = dados.nome();
        if (dados.categoria() == null)
            this.categoria = dados.categoria();
        if (dados.descricao() == null)
            this.descricao = dados.descricao();
        if (dados.precoProd() == null)
            this.precoProduto = dados.precoProd();
    }

}
