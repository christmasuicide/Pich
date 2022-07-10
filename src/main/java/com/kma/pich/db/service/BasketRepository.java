package com.kma.pich.db.service;

import com.kma.pich.db.entity.BasketEntity;
import com.kma.pich.db.entity.ProductEntity;
import com.kma.pich.db.entity.UserBasketId;
import com.kma.pich.db.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasketRepository extends JpaRepository<BasketEntity, UserBasketId> {

    void deleteBasketEntitiesByUser(UserEntity user);
    void deleteAllByProduct(ProductEntity productEntity);

}
