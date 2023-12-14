package com.apapedia.order.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
@JsonIgnoreProperties(value={"order_itemList"}, allowSetters = true)
public class Order {
    @Id
    private UUID id = UUID.randomUUID();
    @NotNull
    @Column(name = "createdAt", nullable = false)
    private Date createdAt;
    @Column(name = "updatedAt", nullable = false)
    private Date updatedAt;
    @NotNull
    @Column(name = "status", nullable = false)
    private Integer status;
    @NotNull
    @Column(name = "totalPrice", nullable = false)
    private Integer totalPrice;
    @NotNull
    @Column(name = "customer", nullable = false)
    private UUID customer;
    @NotNull
    @Column(name = "seller", nullable = false)
    private UUID seller;
    @OneToMany(mappedBy = "orderId", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Order_Item> order_itemList;
}
