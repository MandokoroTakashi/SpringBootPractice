package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.entity.Contact;
import com.example.demo.form.ContactForm;

public interface ContactService {

	void saveContact(ContactForm contactForm);

	void deleteContact(Long id);
	
	void updateContact(Long id, Contact updatedContact);
	
	List<Contact> getContactsAll();
	
	Optional<Contact> getContact(Long id);
	
	
}
