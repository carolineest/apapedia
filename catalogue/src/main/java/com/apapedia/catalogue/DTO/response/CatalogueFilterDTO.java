package com.apapedia.catalogue.DTO.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class CatalogueFilterDTO {
    private Integer price;
    private String productName;
    private String productDescription;
    private String category;
    private Integer stock;
    private String image;
}
