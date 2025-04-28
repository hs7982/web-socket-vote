package com.hseok.vote.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String intro() {
        return "login";
    }

    @GetMapping("/vote")
    public String vote() {
        return "vote";
    }

    @GetMapping("/join")
    public String join() {
        return "join";
    }

    @GetMapping("/createRoom")
    public String createRoom() {
        return "createRoom";
    }

    @GetMapping("/api")
    public ResponseEntity<String> api() {
        return ResponseEntity.ok("API Server");
    }
}