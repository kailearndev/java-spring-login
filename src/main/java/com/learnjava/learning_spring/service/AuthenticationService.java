package com.learnjava.learning_spring.service;

import com.learnjava.learning_spring.dto.request.AuthenticationDTO;
import com.learnjava.learning_spring.dto.request.IntrospectDTO;
import com.learnjava.learning_spring.dto.request.LogoutDTO;
import com.learnjava.learning_spring.dto.request.RefreshTokenDTO;
import com.learnjava.learning_spring.dto.response.AuthenticationResponse;
import com.learnjava.learning_spring.dto.response.IntrospectResponse;
import com.learnjava.learning_spring.entity.InvalidToken;
import com.learnjava.learning_spring.entity.User;
import com.learnjava.learning_spring.exception.AppException;
import com.learnjava.learning_spring.exception.ErrorCode;
import com.learnjava.learning_spring.repository.InvalidTokenRepository;
import com.learnjava.learning_spring.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    InvalidTokenRepository invalidTokenRepository;
    @NonFinal
    @Value("${jwt.signerKey}")
    private String signerKey;

    @NonFinal
    @Value("${jwt.valid-duration}")
    private Long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    private Long REFRESH_DURATION;

    public IntrospectResponse introspect(IntrospectDTO request) throws JOSEException, ParseException {
        var token = request.getToken();
        boolean isValid = true;
        try {
            verifyToken(token, false);
        } catch ( AppException e ) {
              isValid = false;
        }
        return IntrospectResponse.builder()
                .isValid(isValid)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationDTO authenticationDTO) {
        var user = userRepository.findByUsername(authenticationDTO.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        boolean isAuthenticated = passwordEncoder.matches(authenticationDTO.getPassword(), user.getPassword()); // check pass decode
        if (!isAuthenticated) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        var token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .isAuthenticated(true)
                .build();

    }

    private String generateToken(User user) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512); //header

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("kaidev.site")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now()
                        .plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        try {
            jwsObject.sign(new MACSigner(signerKey.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {

            log.error("Error while generating token", e);
            throw new RuntimeException(e);
        }
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(roles -> {
                stringJoiner.add(roles.getName());
                if (!CollectionUtils.isEmpty(roles.getPermissions())) {
                    roles.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
                }
            });
        return stringJoiner.toString();
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws ParseException, JOSEException {
        JWSVerifier verifier = new MACVerifier(signerKey.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);  // check token expired?
        Date expiration = (isRefresh) ?
                new Date(signedJWT.getJWTClaimsSet().getIssueTime().toInstant()
                        .plus(REFRESH_DURATION,ChronoUnit.SECONDS).toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        var isVerified = signedJWT.verify(verifier); // check verified
        if (!(isVerified && expiration.after(new Date()))) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        if (invalidTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHORIZED);
        return signedJWT;
    }

    public void logout(LogoutDTO logoutDTO) throws ParseException, JOSEException {
        var tokenSigned = verifyToken(logoutDTO.getToken(), false);
        String jwId = tokenSigned.getJWTClaimsSet().getJWTID(); // get jwt id
        Date expiration = tokenSigned.getJWTClaimsSet().getExpirationTime(); // get jwt exp time
        InvalidToken invalidToken = InvalidToken.builder()
                .id(jwId)
                .expiryTime(expiration)
                .build();

        invalidTokenRepository.save(invalidToken);
    }

    public AuthenticationResponse refreshToken(RefreshTokenDTO refreshTokenDTO) throws ParseException, JOSEException {
        var signedJWT = verifyToken(refreshTokenDTO.getToken(), true);
        String jwId = signedJWT.getJWTClaimsSet().getJWTID(); // get jwt id
        Date expiration = signedJWT.getJWTClaimsSet().getExpirationTime(); // get jwt exp time
        InvalidToken invalidToken = InvalidToken.builder()
                .id(jwId)
                .expiryTime(expiration)
                .build();
        invalidTokenRepository.save(invalidToken);
        var username = signedJWT.getJWTClaimsSet().getSubject();
        var user = userRepository.findByUsername(username).orElseThrow( () -> new AppException(ErrorCode.USER_NOT_FOUND));
        var token = generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .isAuthenticated(true)
                .build();
    }
}
