package com.apapedia.catalogue.DTO.response;

import com.apapedia.catalogue.model.Category;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class CatalogueUpdateRes {
    private Integer price;
    private String productName;
    private String productDescription;
    private UUID categoryId;
    private Integer stock;
    private String image;
}
