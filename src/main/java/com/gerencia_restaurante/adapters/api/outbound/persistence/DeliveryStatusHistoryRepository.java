package com.gerencia_restaurante.adapters.api.outbound.persistence;
import com.gerencia_restaurante.domain.delivery.DeliveryStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryStatusHistoryRepository extends JpaRepository<DeliveryStatusHistory, Long> { }

