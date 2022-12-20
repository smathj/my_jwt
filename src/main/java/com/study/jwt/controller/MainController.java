package com.study.jwt.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/main")
    public String main() {
        return "main";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }
}
