package com.ead.authuser.services.user.handler;

import com.ead.authuser.dtos.UserRecordDTO;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.exceptions.DuplicatedUsernameException;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.repositories.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class UserHandler {

    @Autowired
    private UserRepository userRepository;

    public UserModel toUserModel(UserRecordDTO userRecordDTO){
        var userModel = new UserModel();
        BeanUtils.copyProperties(userRecordDTO, userModel);
        userModel.setUserStatus(UserStatus.ACTIVE);
        userModel.setUserType(UserType.USER);
        userModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return userModel;
    }

    public void validateUserRecord(UserRecordDTO userRecordDTO){
        validateUsername(userRecordDTO);
    }

    public void validateUsername(UserRecordDTO userRecordDTO){
        if(userRepository.existsByUsername(userRecordDTO.username())){
            throw new DuplicatedUsernameException("Username is already in use");
        }
    }
}
