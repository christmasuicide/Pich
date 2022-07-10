package com.kma.pich.db.service;

import com.kma.pich.db.entity.UserEntity;
import com.kma.pich.type.UserAlreadyExistAuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public UserEntity registerUser(UserEntity userEntity) {
        if (userRepository.existsByLogin(userEntity.getLogin())) {
            throw new UserAlreadyExistAuthenticationException("Username already exists");
        }
        userEntity.setPermissions(new ArrayList<>());
        userEntity.setBaskets(new ArrayList<>());
        return userRepository.saveAndFlush(userEntity);
    }

    @Transactional
    public UserEntity saveUser(UserEntity userEntity) {
        if (userEntity.getPermissions() == null) {
            userEntity.setPermissions(new ArrayList<>());
        }
        if (userEntity.getBaskets() == null) {
            userEntity.setBaskets(new ArrayList<>());
        }
        return userRepository.saveAndFlush(userEntity);
    }

    @Transactional
    public UserEntity getUserById(Integer id) {
        Optional<UserEntity> optionalUser = userRepository.findById(id);

        return optionalUser
                .orElse(null);
    }

    @Transactional
    public boolean isUserExistsByLogin(String login) {
        return userRepository.existsByLogin(login);
    }

    @Transactional
    public Optional<UserEntity> getUserByUsername(String login) {
        return userRepository.findByLogin(login);
    }

}
