package com.gerencia_restaurante.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gerencia_restaurante.domain.entity.Produto;

@Repository
@Transactional
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    public List<Produto> findByNome(String nome);
    Optional<Produto> findByCodigoIfood(String codigoIfood);
}
