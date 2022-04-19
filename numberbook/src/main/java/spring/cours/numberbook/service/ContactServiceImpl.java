package spring.cours.numberbook.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import spring.cours.numberbook.model.Contact;
import spring.cours.numberbook.repository.ContactRepository;

@Service
public class ContactServiceImpl implements ContactService{
	@Autowired
	ContactRepository cr;
	@Override
	public void ajouter(Contact contact) {
		cr.save(contact);
		
	}

	@Override
	public void modifier(Contact contact) {
		Contact c = listerParId(contact.getId());
		if(c != null) {
			c.setFullname(contact.getFullname());
			c.setPhone(contact.getPhone());
			cr.save(c);
		}
		
	}

	@Override
	public void supprimer(int id) {
		cr.deleteById(id);
	}

	@Override
	public Contact listerParId(int id) {
		return cr.findById(id).get();
	}
	
	@Override
	public Contact listerParNom(String fullname) {
		return cr.findByFullName(fullname);
	}
	
	@Override
	public Contact listerParPhone(String phone) {
		return cr.findByPhone(phone);
	}

	@Override
	public List<Contact> listerTout() {
		return cr.findAll();
	}
}
