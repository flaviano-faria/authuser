package com.ead.authuser.dtos;

public record UserRecordDTO(
        String username,
        String email,
        String password,
        String oldPassword,
        String fullName,
        String phoneNumber,
        String imageUrl
) {
}
