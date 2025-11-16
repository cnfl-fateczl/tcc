package com.gerencia_restaurante.application.delivery;

import com.gerencia_restaurante.adapters.api.outbound.persistence.DeliveryOrderRepository;
import com.gerencia_restaurante.application.delivery.dto.DeliveryOrderResponseDto;
import com.gerencia_restaurante.domain.delivery.DeliveryOrder;
import com.gerencia_restaurante.domain.delivery.DeliveryStatusHistory;
import com.gerencia_restaurante.domain.delivery.enums.DeliveryOrderStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeliveryOrderService {

    private final DeliveryOrderRepository orderRepository;

    public DeliveryOrderService(DeliveryOrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

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
            var items = o.getItems().stream().map(i -> {
                var idto = new DeliveryOrderResponseDto.ItemDto();
                idto.setExternalProductId(i.getExternalProductId());
                idto.setName(i.getName());
                idto.setQuantity(i.getQuantity());
                idto.setUnitPrice(i.getUnitPrice());
                idto.setTotalPrice(i.getTotalPrice());
                if (i.getProduto() != null) {
                    idto.setProdutoId(i.getProduto().getId());
                }
                return idto;
            }).collect(Collectors.toList());
            dto.setItems(items);
        }

        // Status history
        if (o.getStatusHistory() != null) {
            var hist = o.getStatusHistory().stream().map(h -> {
                var hDto = new DeliveryOrderResponseDto.StatusHistoryDto();
                hDto.setStatus(h.getStatus());
                hDto.setChangedAt(h.getChangedAt());
                return hDto;
            }).collect(Collectors.toList());
            dto.setStatusHistory(hist);
        }

        return dto;
    }

    public DeliveryOrderResponseDto updateStatus(String id, String newStatusStr) {

        DeliveryOrderStatus newStatus;

        try {
            newStatus = DeliveryOrderStatus.valueOf(newStatusStr.toUpperCase());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status inválido");
        }

        DeliveryOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Pedido não encontrado"
                ));

        DeliveryOrderStatus currentStatus = order.getStatus();

        // 1. status igual → erro
        if (currentStatus == newStatus) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Pedido já está no status " + newStatus
            );
        }

        // 2. validar fluxo permitido (state machine)
        boolean permitido = currentStatus.nextAllowed().contains(newStatus);
        if (!permitido) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Transição inválida: " + currentStatus + " → " + newStatus
            );
        }

        // 3. atualizar status
        order.setStatus(newStatus);

        // 4. registrar histórico
        DeliveryStatusHistory hist = DeliveryStatusHistory.builder()
                .status(newStatus)
                .changedAt(OffsetDateTime.now())
                .order(order)
                .build();

        order.getStatusHistory().add(hist);

        // 5. salvar
        orderRepository.save(order);

        return toDto(order);
    }


}
