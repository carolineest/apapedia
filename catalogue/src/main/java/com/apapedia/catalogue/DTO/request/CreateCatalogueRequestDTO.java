package com.apapedia.catalogue.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateCatalogueRequestDTO {
    private Integer price;
    private String productName;
    private String productDescription;
    private String categoryId;
    private Integer stock;
    private String image;
}