package com.gerencia_restaurante.adapters.api.outbound.persistence;
import com.gerencia_restaurante.domain.delivery.DeliveryOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryOrderRepository extends JpaRepository<DeliveryOrder, String> {
    // findById já vem no JpaRepository; você pode adicionar buscas por status, merchantId, createdAt etc.
}
