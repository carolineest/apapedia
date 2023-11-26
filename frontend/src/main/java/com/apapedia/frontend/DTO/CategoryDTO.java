package com.apapedia.frontend.DTO;

import lombok.*;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class CategoryDTO {
    private UUID id;
    private String name;
}
