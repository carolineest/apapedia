package com.apapedia.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Locale.Category;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CatalogueResponseDTO {
    private UUID id;
    private UUID seller;
    private Integer price;
    private String productName;
    private String productDescription;
    private Category categoryId;
    private Integer stock;
    private String image;
    private boolean isDeleted;
}

