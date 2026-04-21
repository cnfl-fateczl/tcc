package com.gerencia_restaurante.domain.repository;

import com.gerencia_restaurante.domain.entity.ItemdeEstoque;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframwork.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface ItemdeEstoqueRepository extends JpaRepository<ItemdeEstoque, Long> {
    public List<ItemdeEstoque> findByNome(String nome);
}