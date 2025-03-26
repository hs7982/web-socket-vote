package com.hseok.vote.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class UserJoinRequest {
    private String username;
    private String password;
    private String email;
}
