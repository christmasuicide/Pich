package com.kma.pich.db.service;

import com.kma.pich.db.entity.PermissionEntity;
import com.kma.pich.type.Permission;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionEntity getPermission(Permission permission) {
        return permissionRepository.getByPermission(permission);
    }

}
