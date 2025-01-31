package com.learnjava.learning_spring.controllers;

import com.learnjava.learning_spring.dto.request.AuthenticationDTO;
import com.learnjava.learning_spring.dto.request.IntrospectDTO;
import com.learnjava.learning_spring.dto.request.LogoutDTO;
import com.learnjava.learning_spring.dto.request.RefreshTokenDTO;
import com.learnjava.learning_spring.dto.response.ApiResponse;
import com.learnjava.learning_spring.dto.response.AuthenticationResponse;
import com.learnjava.learning_spring.dto.response.IntrospectResponse;
import com.learnjava.learning_spring.exception.SuccessCode;
import com.learnjava.learning_spring.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/token")
    ApiResponse<AuthenticationResponse> authentication(@RequestBody AuthenticationDTO request) {
        var result = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder().code(SuccessCode.SUCCESS.getCode()).data(result).build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> authentication(@RequestBody IntrospectDTO request) throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder().data(result).build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutDTO request) throws ParseException, JOSEException {
          authenticationService.logout(request);
       return ApiResponse.<Void>builder()
               .code(SuccessCode.SUCCESS.getCode()).build();
    }
    @PostMapping("/refresh-token")
    ApiResponse<AuthenticationResponse> refreshToken(@RequestBody RefreshTokenDTO request) throws ParseException, JOSEException {
       var token = authenticationService.refreshToken(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .data(token)
                .code(SuccessCode.SUCCESS.getCode())
                .message("Refresh token successful")
                .build();

    }
}

