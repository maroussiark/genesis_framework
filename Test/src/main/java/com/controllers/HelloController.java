package com.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {
    @GetMapping("/")
    public String crudpage(Model model){
        model.addAttribute("viewpage", "hello");
        return "layout/layout";
    }
}
