package com.apapedia.order.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    private UUID id = UUID.randomUUID();
    @NotNull
    @Column(name = "createdAt", nullable = false)
    private Date createdAt;
    @NotNull
    @Column(name = "updatedAt", nullable = false)
    private Date updatedAt;
    @NotNull
    @Column(name = "status", nullable = false)
    private Integer status;
    @NotNull
    @Column(name = "totalPrice", nullable = false)
    private Integer totalPrice;
    //private Customer customer;
    //private Seller seller;
    @OneToMany(mappedBy = "orderId", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Order_Item> order_itemList;
}
