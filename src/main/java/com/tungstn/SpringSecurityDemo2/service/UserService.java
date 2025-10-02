package com.tungstn.SpringSecurityDemo2.service;

import com.tungstn.SpringSecurityDemo2.model.Users;
import com.tungstn.SpringSecurityDemo2.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepo repo;

    private BCryptPasswordEncoder encoder=new BCryptPasswordEncoder(9);


    public void saveUser(Users user) {
        user.setPassword(encoder.encode(user.getPassword()));
        repo.save(user);
    }

    public List<Users> getUsers() {
        return repo.findAll();
    }

    public List<Users> getUserById(int id) {
        return repo.findAllById(List.of(id));
    }
}
