package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.Contact;
import com.example.demo.form.AdminForm;
import com.example.demo.service.AdminService;
import com.example.demo.service.ContactService;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;
    private final ContactService contactService;

    public AdminController(AdminService adminService ,ContactService contactService) {
    	this.adminService = adminService;
        this.contactService = contactService;
    }

    // サインアップ画面
    @GetMapping("/signup")
    public String signupForm() {
        return "admin/signup"; // signup.htmlを返す
    }

    // サインアップ処理
    @PostMapping("/signup")
    public String signup(@Validated @ModelAttribute("contactForm") AdminForm adminForm, BindingResult errorResult) {
		if(errorResult.hasErrors()) {
			return "adimin/signup";
		}
		adminService.registerAdmin(adminForm);
        return "redirect:/admin/signin";
    }
    
    @GetMapping("/signin")
    public String signinForm() {
        return "admin/signin"; // admin/signin.htmlを返す
    }

    
    // お問い合わせ一覧画面の表示
    @GetMapping("/contacts")
    public String contacts(Model model) {
        List<Contact> contactList = contactService.getContactsAll();
        model.addAttribute("contacts", contactList);
        return "admin/contacts";
    }
    
    // お問い合わせ詳細画面の表示
    @GetMapping("/contacts/{id}")
    public String showContactDetail(@PathVariable Long id, Model model) {
        Optional<Contact> contact = contactService.getContact(id);
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
        Optional<Contact> contact = contactService.getContact(id);
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
    	contactService.updateContact(id, updatedContact);
        return "redirect:/admin/contacts";
    }

    // お問い合わせの削除処理
    @PostMapping("/contacts/{id}/delete")
    public String deleteContact(@PathVariable Long id) {
        contactService.deleteContact(id);
        return "redirect:/admin/contacts";
    }
}
