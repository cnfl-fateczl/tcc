package com.gerencia_restaurante.application.delivery;

import com.gerencia_restaurante.adapters.api.outbound.ifood.dto.IfoodOrderDetailsDto;
import com.gerencia_restaurante.adapters.api.outbound.persistence.DeliveryOrderRepository;
import com.gerencia_restaurante.application.delivery.mapper.IfoodToDomainMapper;
import com.gerencia_restaurante.application.delivery.IfoodOrderClient;
import com.gerencia_restaurante.domain.delivery.DeliveryOrder;
import com.gerencia_restaurante.domain.delivery.DeliveryStatusHistory;
import com.gerencia_restaurante.domain.delivery.enums.DeliveryOrderStatus;
import com.gerencia_restaurante.domain.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DeliveryWebhookProcessor {

    private final DeliveryOrderRepository orderRepository;
    private final ProdutoRepository produtoRepository;
    private final IfoodOrderClient ifoodOrderClient;


    /**
     * PROCESSA PEDIDO COMPLETO (DTO) — usado após o GET /orders/{id}
     */
    @Transactional
    public void processWebhookEvent(IfoodOrderDetailsDto dto, String ifoodRequestId) {

        String orderId = dto.getId();

        // 1) Idempotência
        DeliveryOrder existing = orderRepository.findById(orderId).orElse(null);
        if (existing != null) {
            addStatus(existing, existing.getStatus());
            return;
        }

        // 2) Converter DTO → Domínio
        DeliveryOrder order = IfoodToDomainMapper.toDeliveryOrder(dto);

        // 3) Vincular produtos
        order.getItems().forEach(item ->
                produtoRepository.findByCodigoIfood(item.getExternalProductId())
                        .ifPresent(item::setProduto)
        );

        // 4) Relacionamento bidirecional
        order.getItems().forEach(i -> i.setOrder(order));

        // 5) Status inicial RECEIVED
        addStatus(order, DeliveryOrderStatus.RECEIVED);

        // 6) Salvar pedido
        orderRepository.save(order);

        // 7) Enviar ACK imediato ao iFood
        try {
            ifoodOrderClient.acknowledgeOrder(orderId);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao enviar ACK ao iFood: " + e.getMessage(), e);
        }

        // 8) Atualizar status interno para CONFIRMED
        order.setStatus(DeliveryOrderStatus.CONFIRMED);
        addStatus(order, DeliveryOrderStatus.CONFIRMED);

        orderRepository.save(order);
    }


    /**
     * PROCESSA EVENTOS DE STATUS (confirmação, retirada, cancelamento...)
     * Exemplo: CONFIRMED, CANCELLED, READY_TO_PICKUP, DISPATCHED, CONCLUDED
     */
    @Transactional
    public void processStatusEvent(Map<String, Object> rawEvent) {

        String orderId = (String) rawEvent.get("orderId");
        String fullCode = (String) rawEvent.get("fullCode");

        System.out.println("Evento de status: " + fullCode + " para pedido " + orderId);

        DeliveryOrder order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            System.out.println("⚠ Pedido não encontrado no banco. Ignorando.");
            return;
        }

        // Traduz status recebido
        DeliveryOrderStatus newStatus = DeliveryOrderStatus.fromIfoodCode(fullCode);

        if (newStatus == null) {
            System.out.println("Status não reconhecido: " + fullCode);
            return;
        }

        // Atualizar status
        order.setStatus(newStatus);
        addStatus(order, newStatus);

        orderRepository.save(order);

        System.out.println("Status atualizado para: " + newStatus);
    }


    private void addStatus(DeliveryOrder order, DeliveryOrderStatus status) {
        DeliveryStatusHistory hist = DeliveryStatusHistory.builder()
                .status(status)
                .changedAt(OffsetDateTime.now())
                .order(order)
                .build();

        order.getStatusHistory().add(hist);
    }
}
