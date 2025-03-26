package com.hseok.vote.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserJoinResponse {
    private long userId;
    private String username;
    private String email;
}
