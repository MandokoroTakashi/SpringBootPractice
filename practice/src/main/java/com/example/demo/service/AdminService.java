package com.example.demo.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.demo.entity.Admin;
import com.example.demo.repository.AdminRepository;

public class AdminService {
    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public void registerAdmin(String email, String password, String lastName, String firstName) {
        Admin admin = new Admin();
        admin.setEmail(email);
        admin.setPassword(passwordEncoder.encode(password)); // パスワードのハッシュ化
        admin.setLastName(lastName);
        admin.setFirstName(firstName);
        adminRepository.save(admin);
    }
}