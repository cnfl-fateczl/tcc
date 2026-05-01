package com.gerencia_restaurante.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gerencia_restaurante.domain.entity.ItemdeEstoque;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface ItemdeEstoqueRepository extends JpaRepository<ItemdeEstoque, Long> {
    public List<ItemdeEstoque> findByNome(String nome);
}