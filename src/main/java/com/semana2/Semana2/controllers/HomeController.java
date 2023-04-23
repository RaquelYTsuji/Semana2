package com.semana2.Semana2.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home(){
        return "home";
    }

    @ModelAttribute("servletPath")
    String getRequestServletPath(HttpServletRequest request) {
        System.out.println(request.getServletPath());
        return request.getServletPath();
    }
}
