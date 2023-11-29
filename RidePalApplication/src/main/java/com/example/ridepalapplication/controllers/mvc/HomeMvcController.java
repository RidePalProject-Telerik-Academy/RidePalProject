package com.example.ridepalapplication.controllers.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeMvcController {

    @GetMapping
    public String home() {
        return "home";
    }

    @GetMapping("/main")
    public String mainPage() {
        return "main";
    }
}
