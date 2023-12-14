package com.apapedia.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CatalogueResponseDTO {
    private UUID id;
    private UUID seller;
    private Integer price;
    private String productName;
    private String productDescription;
    private CategoryCatalogueDTO categoryId;
    private Integer stock;
    private String image;
    private boolean deleted;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryCatalogueDTO {
        private UUID id;
        private String name;
    }
}

