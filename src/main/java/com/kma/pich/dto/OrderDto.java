package com.kma.pich.dto;

import com.kma.pich.db.entity.OrderEntity;
import lombok.Data;

import java.util.Date;

@Data
public class OrderDto {

    private Integer id;
    private Date orderDate;
    private String login;
    private String[] orderList;
    private Double orderCost;

    public OrderDto(OrderEntity orderEntity) {
        this.id = orderEntity.getId();
        this.orderDate = orderEntity.getOrderDate();
        this.login = orderEntity.getLogin();
        this.orderList = orderEntity.getOrderList().split("\n");
        this.orderCost = orderEntity.getOrderCost();
    }

}
