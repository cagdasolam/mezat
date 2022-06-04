package com.group7.mezat.controllers;

import com.group7.mezat.responses.ErrorResponse;
import com.group7.mezat.services.UserService;
import com.group7.mezat.documents.User;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.group7.mezat.requests.UserPasswordUpdateRequest;
import com.group7.mezat.responses.UserResponse;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
//@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private UserService userService;

    @GetMapping
    public List<User> getUsers(){
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public UserResponse getOneUser(@PathVariable String id){
        return userService.getOneUser(id);
    }

    @GetMapping("mail/{Email}")
    public UserResponse getOneUserByEmail(@PathVariable String Email){
        return userService.getOneUserByEmail(Email);
    }

    @PostMapping
    public void saveUser(@RequestBody User user){
        userService.saveUser(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id){
        userService.deleteUser(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ErrorResponse> updateUser(@PathVariable String id, @RequestBody UserPasswordUpdateRequest newUser){
        return userService.updateUserPassword(id, newUser);
    }

    @PutMapping("/role/addToUser/{userName}")
    public User addCooperativeRoleToUser(@PathVariable String userName){
        return userService.addCooperativeRoleToUser(userName);
    }

}
