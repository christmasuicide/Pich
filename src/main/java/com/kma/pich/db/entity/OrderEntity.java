package com.kma.pich.db.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@Getter
@Setter
public class OrderEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "order_date")
    @NotNull
    private Date orderDate;

    @Column(name = "login")
    @NotNull
    @Length(max = 40)
    private String login;

    @Column(name = "order_list")
    @NotNull
    private String orderList;

    @Column(name = "order_cost")
    @NotNull
    private Double orderCost;

}
