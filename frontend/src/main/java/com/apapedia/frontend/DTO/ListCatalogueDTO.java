package com.apapedia.frontend.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
import java.util.List;

@Getter
@Setter
public class ListCatalogueDTO {
    private List<CatalogueDTO> listCatalogue;
}

// @Getter
// @Setter
// class CatalogueItemDTO {
// private UUID id;
// private UUID seller;
// private Integer price;
// private String productName;
// private String productDescription;
// private CategoryCatalogueDTO categoryId;
// private Integer stock;
// private String image;
// private Boolean deleted;
// }

// @Getter
// @Setter
// class CategoryCatalogueItemDTO {
// private UUID id;
// private String name;
// }
