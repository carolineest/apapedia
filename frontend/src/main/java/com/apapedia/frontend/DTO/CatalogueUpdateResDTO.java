package com.apapedia.frontend.DTO;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class CatalogueUpdateResDTO {
    private UUID id;
    private Integer price;
    private String productName;
    private String productDescription;
    private UUID categoryId;
    private Integer stock;
    private String image;
}
