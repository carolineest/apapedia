package com.apapedia.catalogue.model;

import java.util.UUID;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "catalogue")
public class Catalogue {
    @Id
    private UUID id = UUID.randomUUID();
    @NotNull
    @Column(name = "seller", nullable = false)
    private UUID seller;
    @NotNull
    @Column(name = "price", nullable = false)
    private Integer price;
    @NotNull
    @Column(name = "productName", nullable = false)
    private String productName;
    @NotNull
    @Column(name = "productDescription", nullable = false)
    private String productDescription;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoryId", referencedColumnName = "id")
    private Category categoryId;
    @NotNull
    @Column(name = "stock", nullable = false)
    private Integer stock;
    @NotNull
    @Column(name = "imageUrl", nullable = false)
    private String image;
}
