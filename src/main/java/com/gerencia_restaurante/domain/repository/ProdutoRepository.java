package com.gerencia_restaurante.domain.repository;

import com.gerencia_restaurante.domain.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
@Transactional
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    public List<Produto> findByNome(String nome);
}
