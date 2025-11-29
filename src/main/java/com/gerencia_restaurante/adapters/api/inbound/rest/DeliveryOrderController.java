package com.gerencia_restaurante.adapters.api.inbound.rest;

import com.gerencia_restaurante.application.delivery.DeliveryOrderService;
import com.gerencia_restaurante.application.delivery.dto.DeliveryOrderResponseDto;
import com.gerencia_restaurante.domain.delivery.enums.DeliveryOrderStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/delivery/orders")
public class DeliveryOrderController {

    private final DeliveryOrderService service;

    public DeliveryOrderController(DeliveryOrderService service) {
        this.service = service;
    }

    @GetMapping
    public List<DeliveryOrderResponseDto> listAll() {
        return service.listAll();
    }

    @GetMapping("/{id}")
    public DeliveryOrderResponseDto getById(@PathVariable String id) {
        return service.findById(id);
    }

    @GetMapping("/status/{status}")
    public List<DeliveryOrderResponseDto> getByStatus(@PathVariable String status) {
        DeliveryOrderStatus s = DeliveryOrderStatus.valueOf(status.toUpperCase());
        return service.findByStatus(s);
    }

    @PatchMapping("/{id}/status")
    public DeliveryOrderResponseDto updateStatus(
            @PathVariable String id,
            @RequestParam String status
    ) {
        return service.updateStatus(id, status);
    }
}
