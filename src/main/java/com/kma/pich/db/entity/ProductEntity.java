package com.kma.pich.db.entity;

import com.kma.pich.type.ProductType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "catalogue")
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "basket")
public class ProductEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title")
    @NotNull
    @NotEmpty(message = "Title can't be empty")
    @Length(max = 60, message = "Title is too long")
    private String title;

    @Column(name = "description")
    @NotNull
    @NotEmpty(message = "Description can't be empty")
    @Length(max = 80, message = "Description is too long")
    private String description;

    @Column(name = "type")
    @NotNull
    @Enumerated(EnumType.STRING)
    private ProductType type;

    @Column(name = "price")
    @NotNull
    private Double price;

    @Column(name = "ingredients")
    @NotNull
    @NotEmpty(message = "Ingredients can't be empty")
    @Length(max = 120, message = "Ingredients is too long")
    private String ingredients;

    @Column(name = "allergens")
    @Length(max = 120, message = "Allergens is too long")
    private String allergens;

    @Column(name = "imageURL")
    @Length(max = 60, message = "imageURL is too long")
    private String imageURL;

    @Column(name = "weight")
    @NotNull
    private Integer weight;

    @Column(name = "gluten_free")
    @NotNull
    private Boolean gluten_free;

    @Column(name = "lactose_free")
    @NotNull
    private Boolean lactose_free;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "product")
    private List<BasketEntity> basket;

}
