package com.buk.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.buk.entities.Bet;
import com.buk.entities.User;

@Stateless
public class BetDAO {
	
	@PersistenceContext
	EntityManager em;
	
	public void insert(Bet bet) {
		em.persist(bet);
	}
	public Bet update(Bet bet) {
		return em.merge(bet);
	}
	public void delete(Bet bet) {
		em.remove(em.merge(bet));
	}
	public Bet get(Object id) {
		return em.find(Bet.class, id);
	}
	public List<Bet> getAllBets(User user){
		List<Bet> bets = new ArrayList<>();
	    Query query = em.createQuery("select b from Bet b where b.user=:user order by id desc");
	    query.setParameter("user", user);
	    
	    try{
	        bets= (List<Bet>) query.getResultList();
	        return bets;
	    }catch (Exception e){
	    	
	        return null;
	    }
	    
	}
	
}

