package com.buk.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.buk.entities.User;

@Stateless
public class UserDAO {
	
	@PersistenceContext
	EntityManager em;
	
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
