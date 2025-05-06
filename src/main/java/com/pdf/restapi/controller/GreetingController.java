package com.pdf.restapi.controller;

import com.pdf.restapi.dto.Greeting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class GreetingController {
    private static final String TEMPLATE = "Hello, %s!";

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        log.error("Inside GreetingController.greeting");
        return new Greeting(String.format(TEMPLATE, name));
    }

}
