package com.buk.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import com.buk.entities.Event;

import javax.persistence.Query;



@Stateless
public class EventDAO {
	
	@PersistenceContext
	EntityManager em;
	
	public void insert(Event event) {
		em.persist(event);
	}
	public Event update(Event event) {
		return em.merge(event);
	}
	public void delete(Event event) {
		em.remove(em.merge(event));
	}
	public Event get(Object id) {
		return em.find(Event.class, id);
	}
	public Event checkEvent(int matchID){
	    Query query = em.createQuery("select e from Event e where matchID=:matchID");
	    query.setParameter("matchID",matchID);
	    try {
	        return (Event) query.getSingleResult();
	    } catch (NoResultException e) {
	        return null;
	    }
	}
	public List<Event> getAllTodaysMatches() {
		return em.createQuery("select e from Event e where e.matchDate >= current_date and e.matchDate < current_date + 1", Event.class)
		        .getResultList();
	}
	
	
	
}
