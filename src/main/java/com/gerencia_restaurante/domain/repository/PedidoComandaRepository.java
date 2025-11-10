package com.gerencia_restaurante.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gerencia_restaurante.domain.entity.PedidoComanda;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface PedidoComandaRepository extends JpaRepository<PedidoComanda, Long>
{
    
}
