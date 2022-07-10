package com.kma.pich.db.service;

import com.kma.pich.db.entity.PermissionEntity;
import com.kma.pich.type.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<PermissionEntity, Integer> {

    PermissionEntity getByPermission(Permission permission);

}
