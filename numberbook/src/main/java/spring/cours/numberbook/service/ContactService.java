package spring.cours.numberbook.service;

import java.util.List;

import spring.cours.numberbook.model.Contact;

public interface ContactService {
	void ajouter(Contact contact);
	void modifier(Contact contact);
	void supprimer(int id);
	Contact listerParId(int id);
	Contact listerParPhone(String phone);
	Contact listerParNom(String fullname);
	List<Contact> listerTout();
}
