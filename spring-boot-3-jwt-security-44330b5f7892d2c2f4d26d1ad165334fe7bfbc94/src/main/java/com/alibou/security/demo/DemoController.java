package com.alibou.security.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo-controller")
@CrossOrigin(origins = "*")

public class DemoController {

  @GetMapping
  public ResponseEntity<String> sayHello() {
    return ResponseEntity.ok("Hello from secured endpoint user");
  }

  @GetMapping("helloAdmin")
  public ResponseEntity<String> sayHello2() {
    return ResponseEntity.ok("Hello from secured endpoint admin");
  }


}
