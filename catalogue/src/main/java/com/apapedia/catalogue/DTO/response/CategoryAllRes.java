package com.apapedia.catalogue.DTO.response;

import com.apapedia.catalogue.model.Category;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class CategoryAllRes {
    private List<Category> categoryList;
}
