package spring.cours.numberbook.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import spring.cours.numberbook.model.Contact;
import spring.cours.numberbook.service.ContactServiceImpl;

@RestController
@RequestMapping("/contacts")
public class ContactController {
	@Autowired
	ContactServiceImpl cs;
	
	@PostMapping
	public void ajouter(@RequestBody Contact contact) {
		cs.ajouter(contact);
	}
	
	@GetMapping
	public List<Contact> getAll() {
		return cs.listerTout();
	}
	
	@GetMapping("/{id}")
	public Contact getContactById(@PathVariable int id) {
		return cs.listerParId(id);
	}
	
	@GetMapping("/number/{phone}")
	public Contact getContactByPhone(@PathVariable String phone) {
		return cs.listerParPhone(phone);
	}
	
	@GetMapping("/name/{fullname}")
	public Contact getContactByFullName(@PathVariable String fullname) {
		return cs.listerParNom(fullname);
	}
}
