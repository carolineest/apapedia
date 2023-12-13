package com.apapedia.frontend.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchFilterDTO {
    private String productName;
    private Integer minPrice;
    private Integer maxPrice;
    private String direction;
    private String attribute;
}
