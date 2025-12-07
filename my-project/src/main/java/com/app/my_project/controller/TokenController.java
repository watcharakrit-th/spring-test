package com.app.my_project.controller;

import org.springframework.http.HttpHeaders;

/*
* Copyright 2020-2021 the original author or authors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* https://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A controller for the token resource.
 *
 * @author Josh Cummings
 */
@RestController
public class TokenController {

    @Autowired
    JwtEncoder encoder;

    @PostMapping("/token")
    public ResponseEntity<String> token(Authentication authentication) {
        Instant now = Instant.now();
        long expiry = 600L;
        // @formatter:off
		String scope = authentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(" "));
        
		JwtClaimsSet claims = JwtClaimsSet.builder()
				.issuer("self")
				.issuedAt(now)
				.expiresAt(now.plusSeconds(expiry))
				.subject(authentication.getName())
				.claim("scope", scope)
				.build();
		// @formatter:on

        // System.out.println("---------------------------");
        // System.out.println("---------------------------");
        // System.out.println(authentication);
        // System.out.println("---------------------------");
        // System.out.println(authentication.getName());
        // System.out.println("---------------------------");
        // System.out.println(authentication.getPrincipal());
        // System.out.println("---------------------------");
        // System.out.println(scope);
        // System.out.println("---------------------------");
        // System.out.println(claims);
        // System.out.println("---------------------------");
        // System.out.println("---------------------------");

        String tokenValue = this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        // 2. Create the HttpOnly Cookie object
        ResponseCookie cookie = ResponseCookie.from("accessToken", tokenValue)
                .httpOnly(true) // <--- THIS IS THE KEY
                .secure(true) // Send only over HTTPS (Required for production)
                .path("/") // Available everywhere on the site
                .maxAge(expiry) // Expires when JWT expires
                .sameSite("Strict") // Prevents CSRF attacks
                .build();

        // 3. Return it in the Header, not the Body
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Login successful"); // Body can be empty or a simple message
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // 1. Create a "Death Cookie"
        ResponseCookie cookie = ResponseCookie.from("accessToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/") // <--- MUST MATCH YOUR LOGIN PATH
                .maxAge(0) // <--- 0 SECONDS = DELETE INSTANTLY
                .sameSite("Strict")
                .build();

        // 2. Send it back
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("You have been logged out");
    }

}