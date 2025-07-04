package com.ead.authuser.services.user.impl;

import com.ead.authuser.dtos.UserRecordDTO;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.exceptions.NotFoundException;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.repositories.UserRepository;
import com.ead.authuser.services.UserService;
import com.ead.authuser.services.user.handler.UserHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    final UserRepository userRepository;

    @Autowired
    private UserHandler userHandler;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserModel> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<UserModel> findById(UUID userId) {
        Optional<UserModel> userModelOptional = userRepository.findById(userId);
        if(userModelOptional.isEmpty()){
            throw new NotFoundException("Error: User not found");
        }
        return userModelOptional;
    }

    @Override
    public void delete(UserModel userModel) {
        userRepository.delete(userModel);
    }

    @Override
    public UserModel registeruser(UserRecordDTO userRecordDTO) {
       userHandler.validateUserRecord(userRecordDTO);
       UserModel userModel = userHandler.toUserModel(userRecordDTO);
        return userRepository.save(userModel);
    }

    @Override
    public UserModel updateUser(UserRecordDTO userRecordDTO, UserModel userModel) {
        userModel.setPhoneNumber(userRecordDTO.phoneNumber());
        userModel.setFullName(userRecordDTO.fullName());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return userRepository.save(userModel);
    }

    @Override
    public UserModel updatePassword(UserRecordDTO userRecordDTO, UserModel userModel) {
        userModel.setPassword(userRecordDTO.password());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return userRepository.save(userModel);
    }

    @Override
    public UserModel updateImage(UserRecordDTO userRecordDTO, UserModel userModel) {
        userModel.setImageUrl(userRecordDTO.imageUrl());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return userRepository.save(userModel);
    }

    @Override
    public Page<UserModel> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
}
