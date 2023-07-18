package com.example.sercurity.model.repository;

import com.example.sercurity.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users,Long> {
     Users findByUserName(String userName);
     boolean existsByUserName(String userName); // kiểm tra xem tên tồn tại chưa
     boolean existsByEmail(String email); // kiểm tra xem email tồn tại chưa


}
