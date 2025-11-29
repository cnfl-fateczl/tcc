package com.gerencia_restaurante.adapters.api.outbound.persistence;
import com.gerencia_restaurante.domain.delivery.DeliveryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryItemRepository extends JpaRepository<DeliveryItem, Long> { }
