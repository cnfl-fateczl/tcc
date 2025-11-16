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

@Service
@RequiredArgsConstructor
public class DeliveryWebhookProcessor {

    private final DeliveryOrderRepository orderRepository;
    private final ProdutoRepository produtoRepository;
    private final IfoodOrderClient ifoodOrderClient;   //

    @Transactional
    public void processWebhookEvent(IfoodOrderDetailsDto dto, String ifoodRequestId) {

        String orderId = dto.getId();

        // 1) Idempotência: se já existe, registra histórico e retorna
        DeliveryOrder existing = orderRepository.findById(orderId).orElse(null);
        if (existing != null) {
            DeliveryStatusHistory hist = DeliveryStatusHistory.builder()
                    .status(existing.getStatus())
                    .changedAt(OffsetDateTime.now())
                    .order(existing)
                    .build();

            existing.getStatusHistory().add(hist);
            orderRepository.save(existing);
            return;
        }

        // 2) Converter DTO -> domínio
        DeliveryOrder order = IfoodToDomainMapper.toDeliveryOrder(dto);

        // 3) Vincular produto local baseado no código do iFood
        order.getItems().forEach(item ->
                produtoRepository.findByCodigoIfood(item.getExternalProductId())
                        .ifPresent(item::setProduto)
        );

        // 4) Acertar relação bidirecional
        if (order.getItems() != null) {
            order.getItems().forEach(i -> i.setOrder(order));
        }

        // 5) Criar status inicial RECEIVED
        DeliveryStatusHistory initial = DeliveryStatusHistory.builder()
                .status(DeliveryOrderStatus.RECEIVED)
                .changedAt(OffsetDateTime.now())
                .order(order)
                .build();

        order.setStatusHistory(java.util.List.of(initial));

        // 6) Salvar o pedido
        orderRepository.save(order);

        // 7) Confirmar automaticamente no iFood
        try {
            ifoodOrderClient.acknowledgeOrder(orderId);
        } catch (Exception e) {
            // Caso o iFood esteja fora, você pode logar, mandar para reprocessamento, etc.
            throw new RuntimeException("Erro ao confirmar pedido no iFood: " + e.getMessage(), e);
        }

        // 8) Atualizar status interno para CONFIRMED
        order.setStatus(DeliveryOrderStatus.CONFIRMED);

        DeliveryStatusHistory confirmedHist = DeliveryStatusHistory.builder()
                .status(DeliveryOrderStatus.CONFIRMED)
                .changedAt(OffsetDateTime.now())
                .order(order)
                .build();

        order.getStatusHistory().add(confirmedHist);

        orderRepository.save(order);
    }
}
