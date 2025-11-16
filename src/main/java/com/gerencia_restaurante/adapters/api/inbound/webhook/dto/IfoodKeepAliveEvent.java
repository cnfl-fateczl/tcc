package com.gerencia_restaurante.adapters.api.inbound.webhook.dto;

public record IfoodKeepAliveEvent(
        String code,
        String fullCode,
        String id
) {}

