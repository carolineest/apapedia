package com.apapedia.catalogue.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="category")
@JsonIgnoreProperties(value={"catalogueList"}, allowSetters = true)
public class Category {
    @Id
    private UUID id = UUID.randomUUID();
    @NotNull
    @Column(name = "nama", nullable = false)
    private String name;
    @OneToMany(mappedBy = "categoryId", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Catalogue> catalogueList;
}
