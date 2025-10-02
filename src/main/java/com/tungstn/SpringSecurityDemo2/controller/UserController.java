package com.tungstn.SpringSecurityDemo2.controller;

import com.tungstn.SpringSecurityDemo2.model.Users;
import com.tungstn.SpringSecurityDemo2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/greet")
    private String greeting(){
        return "Hello User";
    }

    @GetMapping("/getUsers")
    private List<Users> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/getUser/{id}")
    private List<Users> getUserbyId(@PathVariable int id) {
        return userService.getUserById(id);
    }

    @PostMapping("/addUser")
    private Users addUser(@RequestBody Users user){
        userService.saveUser(user);
        return user;
    }



}
