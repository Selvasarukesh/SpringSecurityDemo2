package com.tungstn.SpringSecurityDemo2.controller;

import com.tungstn.SpringSecurityDemo2.model.Users;
import com.tungstn.SpringSecurityDemo2.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping("/login")
    private ResponseEntity<Map<String, String>> login(@RequestBody Users users){

        try {
            Authentication authentication = authManager
                    .authenticate(new UsernamePasswordAuthenticationToken(users.getUsername(), users.getPassword()));

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            String token = jwtUtil.generateToken(userDetails);

            return ResponseEntity.ok(Map.of("token", token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error","Invalid username or password"));
        }
    }
}
