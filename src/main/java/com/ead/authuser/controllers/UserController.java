package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserRecordDTO;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import com.ead.authuser.specifications.SpecificationTemplate;
import com.fasterxml.jackson.annotation.JsonView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/users")
public class UserController {

    final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    Logger logger = LogManager.getLogger(UserController.class);

    @GetMapping
    public ResponseEntity<Page<UserModel>> getAllUsers(
            SpecificationTemplate.UserSpec spec,
            @PageableDefault(page = 0, size = 3, sort = "userId",
                    direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam (required = false) UUID courseId) {

        Page<UserModel> userModelPage = userService.findAll(spec, pageable);

        userModelPage.toList().stream().forEach(
                userModel -> userModel.add(
                        linkTo(methodOn(UserController.class)
                                .getOneUser(userModel.getUserId())).withSelfRel()));
        return ResponseEntity.status(HttpStatus.OK)
                .body(userModelPage);
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
        logger.debug("deleteUser received userId: {}", userId);
        return ResponseEntity.status(HttpStatus.OK).body("User deleted");
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateUser(
            @PathVariable(value = "userId") UUID userId,
            @RequestBody @Validated(UserRecordDTO.UserView.UserPut.class)
            @JsonView(UserRecordDTO.UserView.UserPut.class)  UserRecordDTO userRecordDTO) {

        logger.debug("updateUser received userRecordDTO: {}", userRecordDTO);
        return ResponseEntity.status(HttpStatus.OK).body(
              userService.updateUser(userRecordDTO, userService.findById(userId).get()));
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<Object> updatePassword(
            @PathVariable(value = "userId") UUID userId,
            @RequestBody @Validated(UserRecordDTO.UserView.PasswordPut.class)
            @JsonView(UserRecordDTO.UserView.PasswordPut.class)  UserRecordDTO userRecordDTO) {

        Optional<UserModel> userModelOptional = userService.findById(userId);

        if(userModelOptional.get().getPassword().equals(userRecordDTO.oldPassword())) {
            logger.warn("mismatched old password: {}", userId);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Old password does not match");
        }

        userService.updatePassword(userRecordDTO, userModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Password updated");
    }

    @PutMapping("/{userId}/image")
    public ResponseEntity<Object> updateImage(
            @PathVariable(value = "userId") UUID userId,
            @RequestBody @Validated(UserRecordDTO.UserView.ImagePut.class)
            @JsonView(UserRecordDTO.UserView.ImagePut.class)  UserRecordDTO userRecordDTO) {
        logger.debug("updateImage received userRecordDTO: {}", userRecordDTO);
        return ResponseEntity.status(HttpStatus.OK).body(
                userService.updateImage(userRecordDTO, userService.findById(userId).get() ));
    }
}
