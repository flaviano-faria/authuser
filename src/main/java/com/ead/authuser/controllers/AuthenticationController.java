package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserRecordDTO;
import com.ead.authuser.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth")
public class AuthenticationController {

    Logger logger = LogManager.getLogger(AuthenticationController.class);
    final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/signup")
    public ResponseEntity<Object> registerUser(
            @RequestBody @Validated(UserRecordDTO.UserView.RegistrationPost.class)
            @JsonView(UserRecordDTO.UserView.RegistrationPost.class)
            UserRecordDTO userRecordDTO) {

        logger.debug("POST registerUser received userRecordDTO: {}", userRecordDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.registeruser(userRecordDTO));
    }
}
