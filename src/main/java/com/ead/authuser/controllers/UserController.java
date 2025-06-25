package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserRecordDTO;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserModel>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.findAll());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getOneUser(
            @PathVariable(value = "userId") UUID userId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.findById(userId).get());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "userId") UUID userId) {
        userService.delete(userService.findById(userId).get());
        return ResponseEntity.status(HttpStatus.OK).body("User deleted");
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateUser(
            @PathVariable(value = "userId") UUID userId,
            @RequestBody
            @JsonView(UserRecordDTO.UserView.UserPut.class)  UserRecordDTO userRecordDTO) {

      return ResponseEntity.status(HttpStatus.OK).body(
              userService.updateUser(userRecordDTO, userService.findById(userId).get()));
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<Object> updatePassword(
            @PathVariable(value = "userId") UUID userId,
            @RequestBody
            @JsonView(UserRecordDTO.UserView.PasswordPut.class)  UserRecordDTO userRecordDTO) {

        Optional<UserModel> userModelOptional = userService.findById(userId);

        if(userModelOptional.get().getPassword().equals(userRecordDTO.oldPassword())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Old password does not match");
        }

        userService.updatePassword(userRecordDTO, userModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Password updated");
    }

    @PutMapping("/{userId}/image")
    public ResponseEntity<Object> updateImage(
            @PathVariable(value = "userId") UUID userId,
            @RequestBody
            @JsonView(UserRecordDTO.UserView.ImagePut.class)  UserRecordDTO userRecordDTO) {

        return ResponseEntity.status(HttpStatus.OK).body(
                userService.updateImage(userRecordDTO, userService.findById(userId).get() ));
    }
}
