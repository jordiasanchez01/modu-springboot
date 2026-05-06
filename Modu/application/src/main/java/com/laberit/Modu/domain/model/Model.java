package com.laberit.Modu.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Model {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String image_url;
    private List<Category> categories;
    private List<Product> products;
}
