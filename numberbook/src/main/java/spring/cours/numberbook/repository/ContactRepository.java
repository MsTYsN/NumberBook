package spring.cours.numberbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import spring.cours.numberbook.model.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer>{
	@Query("select c from Contact c where c.fullname=:fullname")
	Contact findByFullName(String fullname);
	@Query("select c from Contact c where c.phone=:phone")
	Contact findByPhone(String phone);
}
