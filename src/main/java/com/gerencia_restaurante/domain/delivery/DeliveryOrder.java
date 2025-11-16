package com.gerencia_restaurante.domain.delivery;
import com.gerencia_restaurante.domain.delivery.enums.DeliveryOrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "delivery_orders")
public class DeliveryOrder {
    @Id
    private String id; // mesmo ID do ifood

    private String displayId;
    private String orderType;
    private String orderTiming;
    private String salesChannel;

    private boolean testOrder;

    @Embedded
    private DeliveryCustomer customer;

    @Embedded
    private DeliveryAddress deliveryAddress;

    private OffsetDateTime createdAt;
    private OffsetDateTime preparationStartTime;

    @Enumerated(EnumType.STRING)
    private DeliveryOrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeliveryItem> items;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeliveryStatusHistory> statusHistory;
}
