package com.hseok.vote.controller;

import com.hseok.vote.user.UserService;
import com.hseok.vote.user.dto.UserJoinRequest;
import com.hseok.vote.user.dto.UserJoinResponse;
import com.hseok.vote.util.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<Object> join(@RequestBody UserJoinRequest reqDto) {
        UserJoinResponse result = userService.userJoin(reqDto);
        return ResponseHandler.responseBuilder(
                HttpStatus.OK,
                "계정이 생성되었습니다.",
                result
        );
    }
}
