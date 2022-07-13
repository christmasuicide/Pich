package com.kma.pich.db.service;

import com.kma.pich.db.entity.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    @Transactional
    public ProductEntity createProduct(ProductEntity productEntity) {
        return productRepository.saveAndFlush(productEntity);
    }

    @Transactional
    public ProductEntity getProductById(Integer id) {
        Optional<ProductEntity> optionalProduct = productRepository.findById(id);
        return optionalProduct
                .orElse(null);
    }

    @Transactional
    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }

    @Transactional
    public List<ProductEntity> getMatchingProducts(ProductEntity productEntity) {
        return productRepository.findAll(Example.of(productEntity));
    }

    @Transactional
    public void removeProduct(ProductEntity productEntity) {
        try {
            Files.deleteIfExists(Path.of(productEntity.getImageURL()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        productRepository.deleteById(productEntity.getId());
    }

}
