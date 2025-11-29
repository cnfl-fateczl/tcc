package com.gerencia_restaurante.application.delivery;

import com.gerencia_restaurante.adapters.api.outbound.ifood.dto.IfoodOrderDetailsDto;
import com.gerencia_restaurante.adapters.api.outbound.persistence.DeliveryOrderRepository;
import com.gerencia_restaurante.adapters.api.outbound.persistence.DeliveryStatusHistoryRepository;
import com.gerencia_restaurante.application.delivery.dto.DeliveryOrderResponseDto;
import com.gerencia_restaurante.application.delivery.mapper.IfoodToDomainMapper;
import com.gerencia_restaurante.domain.delivery.DeliveryItem;
import com.gerencia_restaurante.domain.delivery.DeliveryOrder;
import com.gerencia_restaurante.domain.delivery.DeliveryStatusHistory;
import com.gerencia_restaurante.domain.delivery.enums.DeliveryOrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryOrderService {

    private final DeliveryOrderRepository orderRepository;
    private final DeliveryStatusHistoryRepository historyRepository;
    private final IfoodToDomainMapper mapper;

    // ========================================================
    // API ENDPOINTS (mantidos para seu frontend/postman)
    // ========================================================

    public List<DeliveryOrderResponseDto> listAll() {
        return orderRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public DeliveryOrderResponseDto findById(String id) {
        DeliveryOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado"));
        return toDto(order);
    }

    public List<DeliveryOrderResponseDto> findByStatus(DeliveryOrderStatus status) {
        return orderRepository.findAll()
                .stream()
                .filter(o -> o.getStatus() == status)
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // endpoint PATCH do seu controller
    public DeliveryOrderResponseDto updateStatus(String id, String newStatusStr) {

        DeliveryOrderStatus newStatus;

        try {
            newStatus = DeliveryOrderStatus.valueOf(newStatusStr.toUpperCase());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status inválido");
        }

        DeliveryOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        validateStatusTransition(order.getStatus(), newStatus);
        applyStatus(order, newStatus, newStatusStr);

        return toDto(order);
    }

    // ========================================================
    // MÉTODOS DO FLUXO IFOOD (Webhooks)
    // ========================================================

    @Transactional
    public void createOrUpdateFromIfood(IfoodOrderDetailsDto details,
                                        String rawStatus,
                                        DeliveryOrderStatus normalized) {

        var existing = orderRepository.findById(details.getId());

        if (existing.isPresent()) {
            log.info("[IFOOD] Atualizando pedido existente: {}", details.getId());
            updateExistingOrder(existing.get(), details, rawStatus, normalized);
        } else {
            log.info("[IFOOD] Criando novo pedido: {}", details.getId());
            createNewOrder(details, rawStatus, normalized);
        }

        saveHistory(details.getId(), normalized);
    }

    @Transactional
    public void updateStatus(String orderId,
                             String rawStatus,
                             DeliveryOrderStatus normalized) {

        DeliveryOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado: " + orderId));

        validateStatusTransition(order.getStatus(), normalized);
        applyStatus(order, normalized, rawStatus);

        saveHistory(orderId, normalized);
    }

    // ========================================================
    // IMPLEMENTAÇÃO DETALHADA
    // ========================================================

    private void createNewOrder(IfoodOrderDetailsDto details,
                                String rawStatus,
                                DeliveryOrderStatus normalized) {

        DeliveryOrder mapped = mapper.toDomain(details);

        mapped.setRawStatus(rawStatus);
        mapped.setStatus(normalized);
        mapped.setCreatedAt(OffsetDateTime.now());

        // vincular items ao pedido
        if (mapped.getItems() != null) {
            mapped.getItems().forEach(i -> i.setOrder(mapped));
        }

        orderRepository.save(mapped);
    }

    private void updateExistingOrder(DeliveryOrder entity,
                                     IfoodOrderDetailsDto details,
                                     String rawStatus,
                                     DeliveryOrderStatus normalized) {

        DeliveryOrder mapped = mapper.toDomain(details);

        // campos simples
        entity.setRawStatus(rawStatus);
        entity.setStatus(normalized);
        entity.setDisplayId(mapped.getDisplayId());
        entity.setOrderTiming(mapped.getOrderTiming());
        entity.setOrderType(mapped.getOrderType());
        entity.setSalesChannel(mapped.getSalesChannel());
        entity.setPreparationStartTime(mapped.getPreparationStartTime());

        // endereço
        entity.setDeliveryAddress(mapped.getDeliveryAddress());

        // cliente
        entity.setCustomer(mapped.getCustomer());

        // itens
        entity.getItems().clear();
        for (DeliveryItem i : mapped.getItems()) {
            i.setOrder(entity);
            entity.getItems().add(i);
        }

        orderRepository.save(entity);
    }

    private void saveHistory(String orderId, DeliveryOrderStatus normalized) {
        historyRepository.save(
                DeliveryStatusHistory.builder()
                        .order(orderRepository.getReferenceById(orderId))
                        .status(normalized)
                        .changedAt(OffsetDateTime.now())
                        .build()
        );
    }

    private void validateStatusTransition(DeliveryOrderStatus current, DeliveryOrderStatus next) {

        // 1) CONCLUDED e CANCELLED SEMPRE SÃO ACEITOS
        // Eles podem chegar fora de ordem mesmo.
        if (next == DeliveryOrderStatus.CONCLUDED ||
                next == DeliveryOrderStatus.CANCELLED) {
            return;
        }

        // 2) Se status não mudou → ok
        if (current == next) {
            return;
        }

        // 3) Se transição inválida → apenas avisar, mas NÃO bloquear
        if (!current.nextAllowed().contains(next)) {
            log.warn("[STATUS] Transição inválida: {} → {} (bloqueado para segurança, exceto CONCLUDED/CANCELLED)", current, next);
            throw new RuntimeException("Transição inválida: " + current + " → " + next);
        }
    }


    private void applyStatus(DeliveryOrder order,
                             DeliveryOrderStatus normalizedStatus,
                             String rawStatus) {

        order.setRawStatus(rawStatus);
        order.setStatus(normalizedStatus);
        orderRepository.save(order);
    }

    // ========================================================
    // DTO (mantido do seu projeto original)
    // ========================================================

    private DeliveryOrderResponseDto toDto(DeliveryOrder o) {
        DeliveryOrderResponseDto dto = new DeliveryOrderResponseDto();

        dto.setId(o.getId());
        dto.setDisplayId(o.getDisplayId());
        dto.setOrderType(o.getOrderType());
        dto.setOrderTiming(o.getOrderTiming());
        dto.setSalesChannel(o.getSalesChannel());
        dto.setTestOrder(o.isTestOrder());
        dto.setCreatedAt(o.getCreatedAt());
        dto.setPreparationStartTime(o.getPreparationStartTime());
        dto.setStatus(o.getStatus());

        // Customer
        if (o.getCustomer() != null) {
            var c = new DeliveryOrderResponseDto.CustomerDto();
            c.setCustomerId(o.getCustomer().getCustomerId());
            c.setName(o.getCustomer().getName());
            c.setDocumentNumber(o.getCustomer().getDocumentNumber());
            c.setPhoneNumber(o.getCustomer().getPhoneNumber());
            dto.setCustomer(c);
        }

        // Address
        if (o.getDeliveryAddress() != null) {
            var a = new DeliveryOrderResponseDto.AddressDto();
            a.setStreetName(o.getDeliveryAddress().getStreetName());
            a.setStreetNumber(o.getDeliveryAddress().getStreetNumber());
            a.setNeighborhood(o.getDeliveryAddress().getNeighborhood());
            a.setComplement(o.getDeliveryAddress().getComplement());
            a.setReference(o.getDeliveryAddress().getReference());
            a.setPostalCode(o.getDeliveryAddress().getPostalCode());
            a.setCity(o.getDeliveryAddress().getCity());
            a.setState(o.getDeliveryAddress().getState());
            a.setCountry(o.getDeliveryAddress().getCountry());
            a.setLatitude(o.getDeliveryAddress().getLatitude());
            a.setLongitude(o.getDeliveryAddress().getLongitude());
            dto.setDeliveryAddress(a);
        }

        // Items
        if (o.getItems() != null) {
            dto.setItems(
                    o.getItems().stream().map(i -> {
                        var idto = new DeliveryOrderResponseDto.ItemDto();
                        idto.setExternalProductId(i.getExternalProductId());
                        idto.setName(i.getName());
                        idto.setQuantity(i.getQuantity());
                        idto.setUnitPrice(i.getUnitPrice());
                        idto.setTotalPrice(i.getTotalPrice());
                        return idto;
                    }).collect(Collectors.toList())
            );
        }

        // Status history
        if (o.getStatusHistory() != null) {
            dto.setStatusHistory(
                    o.getStatusHistory().stream().map(h -> {
                        var hdto = new DeliveryOrderResponseDto.StatusHistoryDto();
                        hdto.setStatus(h.getStatus());
                        hdto.setChangedAt(h.getChangedAt());
                        return hdto;
                    }).collect(Collectors.toList())
            );
        }

        return dto;
    }
}
