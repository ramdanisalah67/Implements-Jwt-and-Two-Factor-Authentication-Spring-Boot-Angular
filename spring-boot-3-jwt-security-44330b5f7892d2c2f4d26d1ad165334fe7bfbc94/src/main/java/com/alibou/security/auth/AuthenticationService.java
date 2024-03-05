package com.alibou.security.auth;

import com.alibou.security.config.JwtService;
import com.alibou.security.tfa.TwoFactorAuthenticationService;
import com.alibou.security.user.Role;
import com.alibou.security.user.User;
import com.alibou.security.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final TwoFactorAuthenticationService twoFactorAuthenticationService;
  public AuthenticationResponse register(RegisterRequest request)  {
    var user = User.builder()
        .firstname(request.getFirstname())
        .lastname(request.getLastname())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(Role.ROLE_USER)
            .mfaEnabled(request.isMfaEnabled())
        .build();

    if (request.isMfaEnabled()){
      user.setSecret(twoFactorAuthenticationService.generateNewSecret());
    }

    repository.save(user);
    var jwtToken = jwtService.generateToken(user);
    return AuthenticationResponse.builder()
        .token(jwtToken)
            .secretImageUri(twoFactorAuthenticationService.generateQrCodeImageUri(user.getSecret()))
            .mfaEnabled(request.isMfaEnabled())
            .build();
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
        )
    );
    var user = repository.findByEmail(request.getEmail())
        .orElseThrow();

    if(user.isMfaEnabled()){
      return AuthenticationResponse.builder()
              .token("")
              .mfaEnabled(true)
              .build();
    }
    var jwtToken = jwtService.generateToken(user);

    return AuthenticationResponse.builder()
         .mfaEnabled(false)
        .token(jwtToken)
        .build();
  }

  public AuthenticationResponse verifyCode(VerificationRequest verificationRequest){
  User user = repository.findByEmail(verificationRequest.getEmail()).orElseThrow(()->new EntityNotFoundException(
          String.format("No user Found with %S",verificationRequest.getEmail())
  ));

  if(twoFactorAuthenticationService.isOtpNotValid(user.getSecret(),verificationRequest.getCode())){
    throw  new BadCredentialsException("Code is not correct");
  }
  var jwtToken =jwtService.generateToken(user);
  return AuthenticationResponse.builder()
          .token(jwtToken)
          .mfaEnabled(user.isMfaEnabled())
          .build();
  }
}
