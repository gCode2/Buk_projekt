package com.buk.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.buk.entities.User;


import javax.persistence.Query;

@Stateless
public class UserDAO {
	
	@PersistenceContext
	EntityManager em;
	
public User getUserFromDatabase(String login, String pass) {
	
		
	User u = null;
    Query query = em.createQuery("select u from User u where u.email=:login and u.password=:pass");
    query.setParameter("pass", pass);
    query.setParameter("login", login);

    try{
        u= (User) query.getSingleResult();
        return u;
    }catch (Exception e){
    	
        return null;
    }
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
	
	public boolean checkLogin(String login){
        Query query = em.createQuery("select u from User u where email=:login");
        query.setParameter("login",login);

        try {
            query.getSingleResult();
            return false;
        }catch (Exception e){
            return true;
        }
    }
	
}
