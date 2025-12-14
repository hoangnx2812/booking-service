package com.example.authenservice.service;


import com.example.authenservice.dto.request.LoginUserRequest;
import com.example.authenservice.dto.request.RegisterUserRequest;
import com.example.commericalcommon.dto.request.IdRequest;
import com.example.commericalcommon.dto.request.IntrospectRequest;
import com.example.commericalcommon.dto.response.IntrospectResponse;
import reactor.core.publisher.Mono;

public interface AuthenticationService {

    void registerUser(RegisterUserRequest request);

    Object loginUser(LoginUserRequest request);

    Object getUserProfile();

    Object getAllUserProfiles();

    void deleteUser(IdRequest request);

    Mono<IntrospectResponse> introspectToken(IntrospectRequest request);

}
