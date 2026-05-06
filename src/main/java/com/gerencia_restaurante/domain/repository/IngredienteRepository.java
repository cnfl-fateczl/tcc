package com.gerencia_restaurante.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gerencia_restaurante.domain.entity.Ingrediente;
import com.gerencia_restaurante.domain.entity.IngredienteId;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface IngredienteRepository extends JpaRepository<Ingrediente, IngredienteId> {
    
}
