package com.kma.pich.db.service;

import com.kma.pich.db.entity.ProductEntity;
import com.kma.pich.type.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {



}
