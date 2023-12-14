package com.apapedia.order.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "cart_item")
public class Cart_Item {
    @Id
    private UUID id = UUID.randomUUID();
    @NotNull
    @Column(name = "productId", nullable = false)
    private UUID productId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cartId", referencedColumnName = "id")
    private Cart cartId;
    @NotNull
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
}
