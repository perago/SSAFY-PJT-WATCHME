package com.A108.Watchme.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("home")
    public String home() {
        return "Home";
    }
}
