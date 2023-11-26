package com.apapedia.order.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cart")
public class Cart {
    @Id
    private UUID id = UUID.randomUUID();
    @NotNull
    @Column(name = "userId", nullable = false)
    private UUID userId;
    @NotNull
    @Column(name = "totalPrice", nullable = false)
    private Integer totalPrice = 0;
    @OneToMany(mappedBy = "cartId", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Cart_Item> cart_itemList;
}
