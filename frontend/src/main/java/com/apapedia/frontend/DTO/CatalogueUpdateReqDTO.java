package com.apapedia.frontend.DTO;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class CatalogueUpdateReqDTO {
    private UUID id;
    private Integer price;
    private String productName;
    private String productDescription;
    private CategoryDTO categoryId;
    private Integer stock;
    private String image;
}
