package com.apapedia.catalogue.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="category")
public class Category {
    @Id
    private UUID id = UUID.randomUUID();
    @NotNull
    @Column(name = "nama", nullable = false)
    private String name;
    @OneToMany(mappedBy = "categoryId", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Catalogue> catalogueList;
}
