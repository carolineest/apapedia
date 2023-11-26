package com.apapedia.catalogue.DTO.request;

import com.apapedia.catalogue.model.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class CatalogueUpdateReq {
    private Integer price;
    private String productName;
    private String productDescription;
    private Category categoryId;
    private Integer stock;
    private String image;
}
