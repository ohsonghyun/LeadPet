package com.leadpet.www.presentation.contorller;

import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class HelloWorldController {

    @GetMapping("/helloworld")
    public ResponseEntity<String> helloWorld() {
        return ResponseEntity.ok("hello world");
    }

}
