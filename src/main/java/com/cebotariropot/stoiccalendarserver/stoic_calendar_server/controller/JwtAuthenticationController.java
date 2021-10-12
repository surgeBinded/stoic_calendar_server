package com.cebotariropot.stoiccalendarserver.stoic_calendar_server.controller;

import java.util.Objects;

import com.cebotariropot.stoiccalendarserver.stoic_calendar_server.config.JwtTokenUtil;
import com.cebotariropot.stoiccalendarserver.stoic_calendar_server.exceptions.UserAlreadyExistsException;
import com.cebotariropot.stoiccalendarserver.stoic_calendar_server.model.JwtRequest;
import com.cebotariropot.stoiccalendarserver.stoic_calendar_server.model.JwtResponse;
import com.cebotariropot.stoiccalendarserver.stoic_calendar_server.model.UserDTO;
import com.cebotariropot.stoiccalendarserver.stoic_calendar_server.service.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class JwtAuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsService userDetailsService;

    @Autowired
    public JwtAuthenticationController(final AuthenticationManager authenticationManager, final JwtTokenUtil jwtTokenUtil, final JwtUserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> saveUser(@RequestBody UserDTO user) throws Exception {
        try {
            return ResponseEntity.ok(userDetailsService.save(user));
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.badRequest().body("User already exists!");
        }

    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
