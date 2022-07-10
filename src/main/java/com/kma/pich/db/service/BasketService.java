package com.kma.pich.db.service;

import com.kma.pich.db.entity.BasketEntity;
import com.kma.pich.db.entity.ProductEntity;
import com.kma.pich.db.entity.UserBasketId;
import com.kma.pich.db.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BasketService {
    private final BasketRepository basketRepository;

    @Transactional
    public BasketEntity addToBasket(UserEntity user, ProductEntity product) {
        UserBasketId id = UserBasketId.of(user.getId(), product.getId());
        Optional<BasketEntity> basketEntity = basketRepository.findById(id);
        BasketEntity entity;
        if(basketEntity.isEmpty()) {
            entity = new BasketEntity();
            entity.setId(id);
            entity.setUser(user);
            entity.setProduct(product);
            entity.setQuantity(1);
        } else {
            entity = basketEntity.get();
            entity.setQuantity(entity.getQuantity() + 1);
        }

        return basketRepository.saveAndFlush(entity);
    }

    @Transactional
    public BasketEntity decreaseProductInBasket(UserEntity user, ProductEntity product) {
        UserBasketId id = UserBasketId.of(user.getId(), product.getId());
        Optional<BasketEntity> basketEntity = basketRepository.findById(id);
        BasketEntity entity;
        if(basketEntity.isEmpty()) {
            return null;
        } else {
            entity = basketEntity.get();
            if(entity.getQuantity() == 1) {
                removeFromBasket(user, product);
                return null;
            }
            entity.setQuantity(entity.getQuantity() - 1);
        }
        return basketRepository.saveAndFlush(entity);
    }

    @Transactional
    public void removeFromBasket(UserEntity user, ProductEntity product) {
        UserBasketId id = UserBasketId.of(user.getId(), product.getId());
        if(basketRepository.existsById(id)) {
            basketRepository.deleteById(id);
        }
    }

    @Transactional
    public void clearBasketByUser(UserEntity user) {
        basketRepository.deleteBasketEntitiesByUser(user);
    }

    @Transactional
    public void removeProductFromBaskets(ProductEntity productEntity) {
        basketRepository.deleteAllByProduct(productEntity);
    }


}
