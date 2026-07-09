package com.example.journalApp.service;

import com.example.journalApp.entity.User;
import com.example.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Save User
    public void saveUser(User user) {
        userRepository.save(user);
    }

    // Get All Users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get User by ID
    public Optional<User> findById(ObjectId id) {
        return userRepository.findById(id);
    }

    // Delete User by ID
    public void deleteById(ObjectId id) {
        userRepository.deleteById(id);
    }
    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }
}
