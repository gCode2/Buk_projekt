package com.buk.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.buk.entities.User;

@Stateless
public class UserDAO {
	
	@PersistenceContext
	EntityManager em;
	
public User getUserFromDatabase(String login, String pass) {
		
		User u = null;

		if (login.equals("user1") && pass.equals("password")) {
			u = new User();
			u.setEmail(login);
			u.setPassword(pass);
			u.setName("Jan");
			u.setSurname("Kowalski");
		}

		if (login.equals("user2") && pass.equals("password")) {
			u = new User();
			u.setEmail(login);
			u.setPassword(pass);
			u.setName("Anna");
			u.setSurname("Nowak");
		}

		if (login.equals("user3") && pass.equals("password")) {
			u = new User();
			u.setEmail(login);
			u.setPassword(pass);
			u.setName("Michał");
			u.setSurname("Jaworek");
		}

		return u;
	}
	
	public void insert(User user) {
		em.persist(user);
	}
	public User update(User user) {
		return em.merge(user);
	}
	public void delete(User user) {
		em.remove(em.merge(user));
	}
	public User get(Object id) {
		return em.find(User.class, id);
	}
}
