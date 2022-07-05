package com.kma.pich.dto;

import com.kma.pich.db.entity.ProductEntity;
import lombok.Data;

@Data
public class ProductDto {

    private Integer id;
    private String title;
    private String description;
    private Double price;
    private String ingredients;
    private String allergens;
    private String imageURL;
    private Integer weight;
    private Boolean gluten_free;
    private Boolean lactose_free;

    public ProductDto(ProductEntity productEntity) {
        this.id = productEntity.getId();
        this.title = productEntity.getTitle();
        this.description = productEntity.getDescription();
        this.price = productEntity.getPrice();
        this.ingredients = productEntity.getIngredients();
        this.allergens = productEntity.getAllergens();
        this.imageURL = productEntity.getImageURL();
        this.weight = productEntity.getWeight();
        this.gluten_free = productEntity.getGluten_free();
        this.lactose_free = productEntity.getLactose_free();
    }

}
