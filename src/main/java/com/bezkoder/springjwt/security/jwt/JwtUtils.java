package com.bezkoder.springjwt.security.jwt;

import com.bezkoder.springjwt.security.services.UserDetailsImpl;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;


@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${bezkoder.app.jwtSecret}")
    private String jwtSecret;

    @Value("${bezkoder.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) throws JOSEException {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        JWSSigner signer = new MACSigner(jwtSecret);

        Instant now = Instant.now();
        Date exp = new Date(now.plus(jwtExpirationMs, ChronoUnit.MILLIS).toEpochMilli());

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .issueTime(new Date(now.toEpochMilli()))
                .expirationTime(exp)
                .subject(userPrincipal.getUsername())
                .build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS512), jwtClaimsSet);
        signedJWT.sign(signer);

        return signedJWT.serialize();
    }

    public String validateJwtTokenAndExtractUsername(String jwt) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(jwtSecret);

        SignedJWT signedJWT = SignedJWT.parse(jwt);
        if (!signedJWT.verify(verifier)) {
            throw new IllegalArgumentException("JWT failed verification");
        }

        return signedJWT.getJWTClaimsSet().getSubject();
    }
}
