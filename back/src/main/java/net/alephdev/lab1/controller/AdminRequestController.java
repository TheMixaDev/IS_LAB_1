package net.alephdev.lab1.controller;

import lombok.RequiredArgsConstructor;
import net.alephdev.lab1.annotation.AuthorizedRequired;
import net.alephdev.lab1.annotation.CurrentUser;
import net.alephdev.lab1.enums.Role;
import net.alephdev.lab1.models.AdminRequest;
import net.alephdev.lab1.models.User;
import net.alephdev.lab1.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin-requests")
@RequiredArgsConstructor
@AuthorizedRequired
public class AdminRequestController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<AdminRequest> createAdminRequest(@CurrentUser User user) {
        if (user.getRole() != Role.USER) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if(userService.hasAdminRequest(user)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(userService.createAdminRequest(user), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AdminRequest>> getAdminRequests(@CurrentUser User user) {
        if (user.getRole() != Role.ADMIN) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(userService.getAdminRequests(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> processAdminRequest(@PathVariable Long id, @RequestParam boolean approved, @CurrentUser User user) {
        if (user.getRole() != Role.ADMIN) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        userService.processAdminRequest(id, approved);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}