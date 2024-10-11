package net.alephdev.lab1.controller;

import lombok.RequiredArgsConstructor;
import net.alephdev.lab1.dto.JwtRequest;
import net.alephdev.lab1.dto.JwtResponse;
import net.alephdev.lab1.dto.UserDto;
import net.alephdev.lab1.models.User;
import net.alephdev.lab1.service.UserService;
import net.alephdev.lab1.utils.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody JwtRequest authenticationRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );

            if (authentication.isAuthenticated()) {
                String username = ((UserDetails) authentication.getPrincipal()).getUsername();
                User user = userService.findByUsername(username);

                String token = jwtUtils.generateToken(user);
                return ResponseEntity.ok(new JwtResponse(token));
            } else {
                return new ResponseEntity<>("Неверные учетные данные", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Произошла ошибка при аутентификации: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserDto userDto) {
        try {
            return new ResponseEntity<>(userService.register(userDto), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}