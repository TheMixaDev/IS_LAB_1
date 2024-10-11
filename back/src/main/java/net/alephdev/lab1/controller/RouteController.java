package net.alephdev.lab1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RouteController {
    @RequestMapping(value = "/{path:^(?!ws$)[^.]*}")
    public String redirect() {
        return "forward:/";
    }
}
