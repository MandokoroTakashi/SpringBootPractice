package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Admin;
import com.example.demo.form.AdminForm;
import com.example.demo.repository.AdminRepository;

@Service
public class AdminServiceImp implements AdminService {
	@Autowired
	    private AdminRepository adminRepository;
	    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void registerAdmin(AdminForm adminForm) {
        Admin admin = new Admin();
        admin.setEmail(adminForm.getEmail());
        admin.setPassword(passwordEncoder.encode(adminForm.getPassword()));
        admin.setLastName(adminForm.getLastName());
        admin.setFirstName(adminForm.getFirstName());
        adminRepository.save(admin);
    }
}