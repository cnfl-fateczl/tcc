package com.gerencia_restaurante.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gerencia_restaurante.domain.entity.ItemPedido;
import com.gerencia_restaurante.domain.entity.ItemPedidoId;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, ItemPedidoId>
{
    
}
