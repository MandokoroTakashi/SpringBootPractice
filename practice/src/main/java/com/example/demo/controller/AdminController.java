package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Admin;
import com.example.demo.entity.Contact;
import com.example.demo.repository.AdminRepository;
import com.example.demo.repository.ContactRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
	private final ContactRepository contactRepository;

    public AdminController(ContactRepository contactRepository ,AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.contactRepository = contactRepository;
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // サインアップ画面
    @GetMapping("/signup")
    public String signupForm() {
        return "admin/signup"; // signup.htmlを返す
    }

    // サインアップ処理
    @PostMapping("/signup")
    public String signup(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password,
            Model model) {
        Admin admin = new Admin();
        admin.setFirstName(firstName);
        admin.setLastName(lastName);
        admin.setEmail(email);
        admin.setPassword(passwordEncoder.encode(password)); // パスワードのハッシュ化
        adminRepository.save(admin);
        model.addAttribute("message", "管理者登録が完了しました！");
        return "redirect:/admin/signin";
    }
    
    @GetMapping("/signin")
    public String signinForm() {
        return "admin/signin"; // admin/signin.htmlを返す
    }

    
    // お問い合わせ一覧画面の表示
    @GetMapping("/contacts")
    public String contacts(Model model) {
        List<Contact> contactList = contactRepository.findAll();
        model.addAttribute("contacts", contactList);
        return "admin/contacts";
    }
    
    // お問い合わせ詳細画面の表示
    @GetMapping("/contacts/{id}")
    public String showContactDetail(@PathVariable Long id, Model model) {
        Optional<Contact> contact = contactRepository.findById(id);
        if (contact.isPresent()) {
            model.addAttribute("contact", contact.get());
            return "admin/contact_detail";
        } else {
            return "redirect:/admin/contacts"; // 該当IDがなければ一覧画面にリダイレクト
        }
    }
    
    // 編集画面の表示
    @GetMapping("/contacts/{id}/edit")
    public String editContact(@PathVariable Long id, Model model) {
        Optional<Contact> contact = contactRepository.findById(id);
        if (contact.isPresent()) {
            model.addAttribute("contact", contact.get());
            return "admin/contact_edit";
        } else {
            return "redirect:/admin/contacts";
        }
    }
    
    // お問い合わせの更新処理
    @PostMapping("/contacts/{id}/edit")
    public String updateContact(@PathVariable Long id, @ModelAttribute Contact updatedContact) {
        contactRepository.findById(id).ifPresent(contact -> {
            contact.setLastName(updatedContact.getLastName());
            contact.setFirstName(updatedContact.getFirstName());
            contact.setEmail(updatedContact.getEmail());
            contact.setPhone(updatedContact.getPhone());
            contact.setZipCode(updatedContact.getZipCode());
            contact.setAddress(updatedContact.getAddress());
            contact.setBuildingName(updatedContact.getBuildingName());
            contact.setContactType(updatedContact.getContactType());
            contact.setBody(updatedContact.getBody());
            contactRepository.save(contact);
        });
        return "redirect:/admin/contacts";
    }

    // お問い合わせの削除処理
    @PostMapping("/contacts/{id}/delete")
    public String deleteContact(@PathVariable Long id) {
        contactRepository.deleteById(id);
        return "redirect:/admin/contacts";
    }
}
