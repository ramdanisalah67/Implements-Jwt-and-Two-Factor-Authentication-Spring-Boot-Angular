package com.alibou.security.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;

  @PostMapping("/register")
  public ResponseEntity<?> register(
      @RequestBody RegisterRequest request
  )
  {
    var response = service.register(request);
    System.out.println("registrer");
    if(request.isMfaEnabled()){
      return  ResponseEntity.ok(response);
    }
    return ResponseEntity.accepted().build();
  }
  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
      @RequestBody AuthenticationRequest request
  ) {
    return ResponseEntity.ok(service.authenticate(request));
  }

  @PostMapping("/verify")
public ResponseEntity<?> verifyCode(@RequestBody VerificationRequest verificationRequest){

    return  ResponseEntity.ok(service.verifyCode(verificationRequest));
  }

}
