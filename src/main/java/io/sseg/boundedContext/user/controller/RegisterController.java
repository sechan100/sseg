package io.sseg.boundedContext.user.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegisterController {
    
    
    @GetMapping("/register")
    public String registerPage() {
        return "/register";
    }
    
    
    
}
