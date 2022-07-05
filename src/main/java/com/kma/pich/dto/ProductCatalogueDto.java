package com.kma.pich.dto;

import com.kma.pich.db.entity.ProductEntity;
import lombok.Data;

@Data
public class ProductCatalogueDto {

    private Integer id;
    private String title;
    private String description;
    private Double price;
    private String imageURL;

    public ProductCatalogueDto(ProductEntity productEntity) {
        this.id = productEntity.getId();
        this.title = productEntity.getTitle();
        this.description = productEntity.getDescription();
        this.price = productEntity.getPrice();
        this.imageURL = productEntity.getImageURL();
    }

}
