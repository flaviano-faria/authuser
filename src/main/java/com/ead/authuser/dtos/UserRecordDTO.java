package com.ead.authuser.dtos;

import com.ead.authuser.validations.PasswordConstraint;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRecordDTO(
        @NotBlank(groups = UserView.RegistrationPost.class,
                message = "username is required")
        @Size(groups = UserView.RegistrationPost.class,
                min = 4, max = 50, message = "username size must be between 4 and 50")
        @JsonView(UserView.RegistrationPost.class)
        String username,

        @NotBlank(groups = UserView.RegistrationPost.class,
                message = "email is required")
        @Email(groups = UserView.RegistrationPost.class,
                message = "email must have a valid format")
        @JsonView(UserView.RegistrationPost.class)
        String email,

        @NotBlank(groups = {UserView.RegistrationPost.class, UserView.PasswordPut.class},
                message = "password is required")
        @Size(groups = {UserView.RegistrationPost.class, UserView.PasswordPut.class},
                min = 6, max = 20, message = "size must be between 6 and 20")
        @PasswordConstraint(groups = {UserView.RegistrationPost.class, UserView.PasswordPut.class})
        @JsonView({UserView.RegistrationPost.class, UserView.PasswordPut.class})
        String password,

        @NotBlank(groups = UserView.PasswordPut.class, message = "old password is required")
        @Size(groups = UserView.PasswordPut.class,
                min = 6, max = 20, message = "size must be between 6 and 20")
        @PasswordConstraint(groups = {UserView.PasswordPut.class})
        @JsonView(UserView.PasswordPut.class)
        String oldPassword,

        @NotBlank(groups = {UserView.RegistrationPost.class, UserView.RegistrationPost.class},
                message = "fullName is required")
        @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
        String fullName,

        @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
        String phoneNumber,

        @NotBlank(groups = UserView.ImagePut.class, message = "imageUrl is required")
        @JsonView(UserView.ImagePut.class)
        String imageUrl
) {
    public interface UserView{
        interface RegistrationPost{}
        interface UserPut{}
        interface PasswordPut{}
        interface ImagePut{}
    }
}
