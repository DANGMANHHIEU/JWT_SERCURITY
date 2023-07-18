package com.example.sercurity.model.service;

import com.example.sercurity.model.entity.Users;

public interface UserService {
    Users findByUserName(String userName);
    boolean existsByUserName(String userName); // kiểm tra xem tên tồn tại chưa
    boolean existsByEmail(String email); // kiểm tra xem email tồn tại chưa

    Users saveOrUpdate(Users users);
//    Iterable<Users> findALL();

}
