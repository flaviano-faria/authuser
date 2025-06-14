package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserRecordDTO;
import com.ead.authuser.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth")
public class AuthenticationController {
    final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/signup")
    public ResponseEntity<Object> registerUser(
            @RequestBody UserRecordDTO userRecordDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.registeruser(userRecordDTO));
    }
}
