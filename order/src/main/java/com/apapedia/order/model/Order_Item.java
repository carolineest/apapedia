package com.apapedia.order.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_item")
public class Order_Item {
    @Id
    private UUID id = UUID.randomUUID();
    @NotNull
    @Column(name = "productId", nullable = false)
    private UUID productId;
    @NotNull
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
    @NotNull
    @Column(name = "productName", nullable = false)
    private String productName;
    @NotNull
    @Column(name = "productPrice", nullable = false)
    private Integer productPrice;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "orderId", referencedColumnName = "id")
    private Order orderId;
}
