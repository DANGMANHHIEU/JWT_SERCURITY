package com.example.sercurity.model.seviceImp;

import com.example.sercurity.model.entity.Users;
import com.example.sercurity.model.repository.UserRepository;
import com.example.sercurity.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImp implements UserService {
    @Autowired

private UserRepository userRepository;
    @Override
    public Users findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public boolean existsByUserName(String userName) {
        return userRepository.existsByUserName(userName);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Users saveOrUpdate(Users users) {
        return userRepository.save(users);
    }

//    @Override
//    public Iterable<Users> findALL() {
//        return userRepository.findAll();
//    }
}
