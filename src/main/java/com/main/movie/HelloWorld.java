package com.main.movie;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorld {
    @RequestMapping("")
    public String index(){
        return "Main page";
    }

    @RequestMapping("/Hello")
    public String hello(){
        return "Hello World!";
    }
}
