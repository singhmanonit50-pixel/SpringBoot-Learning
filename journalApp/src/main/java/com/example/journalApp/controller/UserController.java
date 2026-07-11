package com.example.journalApp.controller;

import com.example.journalApp.entity.User;
import com.example.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user1")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUser() {
        return userService.getAllUsers();
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        if (user.getUsername() == null || user.getUsername().isBlank()
                || user.getPassword() == null || user.getPassword().isBlank()) {
            return ResponseEntity.badRequest().body("Username and password are required");
        }

        try {
            User saved = userService.saveUser(user);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Username already taken: " + user.getUsername());
        }
    }

    @PutMapping("/{username}")
    public ResponseEntity<?> updateUser(@RequestBody User user,
                                        @PathVariable("username") String username) {

        Optional<User> existingOpt = userService.findByUsername(username);

        if (existingOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User userInDb = existingOpt.get();

        // Only update username if provided and different
        if (user.getUsername() != null && !user.getUsername().isBlank()) {
            userInDb.setUsername(user.getUsername());
        }

        // Only update password if provided — avoids wiping it out or NPEs
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            userInDb.setPassword(userService.encodePassword(user.getPassword()));
        }

        User updated = userService.updateExistingUser(userInDb);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        userService.deleteById(new org.bson.types.ObjectId(id));
        return ResponseEntity.noContent().build();
    }
}

//controller ----> service ----> repository
