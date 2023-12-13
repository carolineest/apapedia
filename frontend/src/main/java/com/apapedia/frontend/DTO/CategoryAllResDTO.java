package com.apapedia.frontend.DTO;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class CategoryAllResDTO {
    private List<CategoryDTO> categoryList;
}
