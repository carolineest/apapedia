package com.apapedia.frontend.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CatalogueDTO {
    private UUID id;
    private UUID seller;
    private Integer price;
    private String productName;
    private String productDescription;
    private CategoryDTO categoryId;
    private Integer stock;
    private String image;
}

@Getter
@Setter
class CategoryDTO {
    private UUID id;
    private String name;
}
