package com.kma.pich.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kma.pich.db.entity.PermissionEntity;
import com.kma.pich.db.entity.UserEntity;
import com.kma.pich.db.service.PermissionService;
import com.kma.pich.db.service.UserService;
import com.kma.pich.type.AdminCredentials;
import com.kma.pich.type.Permission;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AdminConfig {

    private final UserService userService;
    private final PermissionService permissionService;
    private final ObjectMapper objectMapper;
    private final MyPasswordEncoder myPasswordEncoder;

    @PostConstruct
    public void init(){
        try {
            File credentialsFile = new File("credentials.json");
            if(!credentialsFile.exists()) {
                System.out.println("No credentials file found. Admin initialization skipped");
                return;
            }
            AdminCredentials[] credentials = objectMapper.readValue(credentialsFile, AdminCredentials[].class);
            PermissionEntity adminPermission = permissionService.getPermission(Permission.ADMIN);
            for (AdminCredentials credential : credentials) {
                System.out.println(credential);
                Optional<UserEntity> existingUser = userService.getUserByUsername(credential.getUsername());
                if(existingUser.isPresent()) {
                    if(!existingUser.get().getPermissions().contains(adminPermission)) {
                        System.out.println("Error giving admin permission to " + credential.getUsername() + ". Not admin user with same username already exists");
                        continue;
                    }
                }
                UserEntity userEntity;
                if(existingUser.isPresent()) {
                    userEntity = existingUser.get();
                    userEntity.setPassword(myPasswordEncoder.encode(credential.getPassword()));
                } else {
                    userEntity = UserEntity.builder()
                            .login(credential.getUsername())
                            .password(myPasswordEncoder.encode(credential.getPassword()))
                            .permissions(List.of(adminPermission))
                            .build();
                }
                userService.saveUser(userEntity);
            }
        } catch (IOException e) {
            System.out.println("Error while reading configuration");
            e.printStackTrace();
        }
    }

}
