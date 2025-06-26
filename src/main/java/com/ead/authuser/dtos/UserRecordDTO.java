package com.ead.authuser.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRecordDTO(
        @NotBlank(message = "username is required")
        @Size(min = 4, max = 50, message = "username size must be between 4 and 50")
        @JsonView(UserView.RegistrationPost.class)
        String username,

        @NotBlank(message = "email is required")
        @Email(message = "email must have a valid format")
        @JsonView(UserView.RegistrationPost.class)
        String email,

        @NotBlank(message = "password is required")
        @Size(min = 6, max = 20, message = "size must be between 6 and 20")
        @JsonView({UserView.RegistrationPost.class, UserView.PasswordPut.class})
        String password,


        @JsonView(UserView.PasswordPut.class)
        String oldPassword,
        @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
        String fullName,
        @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
        String phoneNumber,
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
