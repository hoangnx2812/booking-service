package com.example.authenservice.service.impl;


import com.example.authenservice.configuration.KeycloakProperties;
import com.example.authenservice.dto.request.LoginUserRequest;
import com.example.authenservice.dto.request.RegisterUserRequest;
import com.example.authenservice.dto.response.LoginUserResponse;
import com.example.authenservice.entity.UserAuth;
import com.example.authenservice.entity.UserInfo;
import com.example.authenservice.repository.UserAuthRepository;
import com.example.authenservice.repository.UserInfoRepository;
import com.example.authenservice.service.AuthenticationService;
import com.example.commericalcommon.dto.request.IdRequest;
import com.example.commericalcommon.dto.request.IntrospectRequest;
import com.example.commericalcommon.dto.response.IntrospectResponse;
import com.example.commericalcommon.exception.ErrorCode;
import com.example.commericalcommon.exception.GlobalException;
import com.example.commericalcommon.utils.AuthorityConstant;
import com.example.commericalcommon.utils.Constant;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.ws.rs.core.Response;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;

import static com.example.commericalcommon.utils.Constant.PrefixNo.USER_NO;
import static com.example.commericalcommon.utils.Util.*;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    KeycloakProperties keycloakProperties;
    UserAuthRepository userAuthRepository;
    UserInfoRepository userInfoRepository;
    Keycloak adminKeycloak;
    WebClient webClient;

    public AuthenticationServiceImpl(KeycloakProperties keycloakProperties,
                                     UserAuthRepository userAuthRepository,
                                     UserInfoRepository userInfoRepository,
                                     @Qualifier("adminKeycloak")
                                     Keycloak adminKeycloak, WebClient webClient) {
        this.keycloakProperties = keycloakProperties;
        this.userAuthRepository = userAuthRepository;
        this.userInfoRepository = userInfoRepository;
        this.adminKeycloak = adminKeycloak;
        this.webClient = webClient;
    }

    @Override
    @Transactional
    public void registerUser(RegisterUserRequest request) {
        String salt = generateSalt(null);
        UserAuth user = new UserAuth();
        user.setUserName(request.getUsername());
        user.setUserPwdHash(encryptSHA256(request.getPassword() + salt));
        user.setUserSalt(salt);
        userAuthRepository.save(user);

        UserInfo userInfo = new UserInfo();
        userInfo.setFirstName(request.getFirstName());
        userInfo.setMiddleName(request.getMiddleName());
        userInfo.setLastName(request.getLastName());
        userInfo.setFullName(request.getFirstName() + " " + request.getMiddleName() + " " + request.getLastName());
        userInfo.setBirthday(request.getBirthDate());
        userInfo.setEmail(request.getEmail());
        userInfo.setPhone(request.getUsername());
        userInfo.setUserNo(generateNo(USER_NO));
        userInfo.setUserAuth(user);
        userInfoRepository.save(userInfo);

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(request.getUsername());
        userRepresentation.setEmail(request.getEmail());
        userRepresentation.setFirstName(request.getFirstName());
        userRepresentation.setLastName(request.getLastName());
        userRepresentation.setEmailVerified(true);
        userRepresentation.setEnabled(true);
        userRepresentation.setRequiredActions(Collections.emptyList());

        try (Response response = adminKeycloak.realm(keycloakProperties.getRealm())
                .users()
                .create(userRepresentation)) {
            if (HttpStatus.CREATED.value() == response.getStatus()) {
                String location = response.getLocation().toString();
                String userId = location.substring(location.lastIndexOf("/") + 1);

                user.setUserInfoNo(userInfo.getUserNo());
                user.setUserInfoId(userInfo.getId());
                user.setKeycloakId(userId);
                userAuthRepository.save(user);

                //Them pass
                CredentialRepresentation credential = new CredentialRepresentation();
                credential.setTemporary(false);
                credential.setType(CredentialRepresentation.PASSWORD);
                credential.setValue(request.getPassword());
                adminKeycloak.realm(keycloakProperties.getRealm())
                        .users()
                        .get(userId)
                        .resetPassword(credential);

                //Them role mac dinh la USER
                UserResource userResource = adminKeycloak
                        .realm(keycloakProperties.getRealm())
                        .users()
                        .get(userId);
                RoleRepresentation generalRole = adminKeycloak
                        .realm(keycloakProperties.getRealm())
                        .roles()
                        .get(Constant.DefaultRole.USER)
                        .toRepresentation();
                userResource.roles().realmLevel().add(Collections.singletonList(generalRole));

                log.info("User created in Keycloak: {}", request.getUsername());
            } else {
                String errorBody = response.readEntity(String.class);
                log.error("Failed to create user in Keycloak: {}", errorBody);
                throw new GlobalException(ErrorCode.INVALID_INPUT);
            }
        }
    }

    @Override
    public Object loginUser(LoginUserRequest request) {
        UserAuth user = getUserAuthByUsername(request.getUsername());

        String hashedInputPassword = encryptSHA256(request.getPassword() + user.getUserSalt());
        boolean authenticated = user.getUserPwdHash().equals(hashedInputPassword);

        if (!authenticated) throw new GlobalException(ErrorCode.UNAUTHENTICATED);

        try (Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(keycloakProperties.getUrls())
                .realm(keycloakProperties.getRealm())
                .clientId(keycloakProperties.getClientId())
                .clientSecret(keycloakProperties.getClientSecret())
                .username(request.getUsername())
                .password(hashedInputPassword)
                .grantType(OAuth2Constants.PASSWORD)
                .build()) {

            AccessTokenResponse tokenResponse = keycloak.tokenManager().getAccessToken();

            return LoginUserResponse.builder()
                    .accessToken(tokenResponse.getToken())
                    .refreshToken(tokenResponse.getRefreshToken())
                    .build();

        } catch (jakarta.ws.rs.WebApplicationException ex) {
            log.error("HTTP Error: {}", ex.getResponse().getStatus());
            String body = ex.getResponse().readEntity(String.class);
            log.error("Response body: {}", body);
            throw new GlobalException(ErrorCode.UNCATEGORIZED, body);

        } catch (Exception e) {
            log.error("Login failed cause: ", e);
            throw new GlobalException(ErrorCode.UNCATEGORIZED);
        }
    }

    @Override
    public Object getUserProfile() {
        return null;
    }

    @Override
    public Object getAllUserProfiles() {
        return null;
    }

    @Override
    @Transactional
    @PreAuthorize(AuthorityConstant.DefaultRole.ADMIN)
    public void deleteUser(IdRequest request) {
        UserInfo userInfo = userInfoRepository.findByUserAuthId(request.getId())
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_EXISTED));
        String keyCloakId = userInfo.getUserAuth().getKeycloakId();
        userInfoRepository.delete(userInfo);
        userAuthRepository.deleteById(request.getId());

        try (Response response = adminKeycloak.realm(keycloakProperties.getRealm())
                .users()
                .delete(keyCloakId)) {
            if (HttpStatus.NO_CONTENT.value() != response.getStatus()) {
                String errorBody = response.readEntity(String.class);
                log.error("Failed to deleted user in Keycloak: {}", errorBody);
                throw new GlobalException(ErrorCode.UNCATEGORIZED);
            }
        }
    }

    @Override
    public Mono<IntrospectResponse> introspectToken(IntrospectRequest request) {
        String url = keycloakProperties.getUrls()
                + "/realms/"
                + keycloakProperties.getRealm()
                + "/protocol/openid-connect/token/introspect";
        IntrospectResponse introspectResponse = new IntrospectResponse();
        return webClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .headers(h ->
                        h.setBasicAuth(keycloakProperties.getClientId(), keycloakProperties.getClientSecret()))
                .bodyValue("token=" + request.getToken())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(response -> {
                    boolean active = response.get("active").asBoolean();
                    introspectResponse.setValid(active);
                    return introspectResponse;
                })
                .switchIfEmpty(Mono.just(IntrospectResponse.builder().valid(false).build()))
                .doOnError(throwable -> log.error("Error during token introspection: ", throwable))
                .onErrorResume(e -> {
                    log.error("Token verification failed: ", e);
                    return Mono.error(new GlobalException(ErrorCode.UNAUTHENTICATED));
                });
    }

    private UserAuth getUserAuthByUsername(String username) {
        return userAuthRepository.findByUserName(username)
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_EXISTED));
    }
}

