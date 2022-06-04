package com.group7.mezat.requests;

import lombok.Data;

@Data
public class UserPasswordUpdateRequest {
    private String oldPassword;
    private String password;
}
