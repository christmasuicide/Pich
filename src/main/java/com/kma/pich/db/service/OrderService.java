package com.kma.pich.db.service;

import com.kma.pich.db.entity.OrderEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional
    public OrderEntity createOrder(OrderEntity orderEntity) {
        return orderRepository.saveAndFlush(orderEntity);
    }

    @Transactional
    public List<OrderEntity> getAllOrders() {
        return orderRepository.findAllByOrderByIdDesc();
    }

}
