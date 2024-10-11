package net.alephdev.lab1.controller;

import net.alephdev.lab1.annotation.AuthorizedRequired;
import net.alephdev.lab1.annotation.CurrentUser;
import net.alephdev.lab1.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@AuthorizedRequired
public class UserController {

    @GetMapping
    public ResponseEntity<User> getCurrentUser(@CurrentUser User user) {
        return ResponseEntity.ok(user);
    }
}