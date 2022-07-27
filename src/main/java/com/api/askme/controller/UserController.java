package com.api.askme.controller;

import com.api.askme.entity.User;
import com.api.askme.exception.UserNotFoundException;
import com.api.askme.response.UserResponse;
import com.api.askme.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponse> getAllUsers(){
        return userService.getAllUsers().stream().map(u -> new UserResponse(u)).collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody User newUser) {
        User user = userService.saveOneUser(newUser);
        if(user != null)
            return new ResponseEntity<>(HttpStatus.CREATED);
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/{userId}")
    public UserResponse getOneUser(@PathVariable Long userId) {
        User user = userService.getOneUserById(userId);
        if(user == null) {
            throw new UserNotFoundException();
        }
        return new UserResponse(user);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateOneUser(@PathVariable Long userId, @RequestBody User newUser) {
        User user = userService.updateOneUser(userId, newUser);
        if(user != null)
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @DeleteMapping("/{userId}")
    public void deleteOneUser(@PathVariable Long userId) {
        userService.deleteById(userId);
    }

    @GetMapping("/activity/{userId}")
    public List<Object> getUserActivity(@PathVariable Long userId) {
        return userService.getUserActivity(userId);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private void handleUserNotFound() {

    }
}
