package com.gerencia_restaurante.adapters.api.inbound.webhook.dto;

import lombok.Data;

@Data
public class EventDto {

    private String code;        // Ex: "PLC"
    private String fullCode;    // Ex: "PLACED"
    private String id;          // id do próprio evento
    private String orderId;     // ID DO PEDIDO (importantíssimo!)
    private String merchantId;
    private String salesChannel;
    private String createdAt;   // timestamp
}

