package com.buk.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.buk.entities.Bet;
import com.buk.entities.Betitem;
import com.buk.entities.User;

@Stateless
public class BetitemDAO {
	
	@PersistenceContext
	EntityManager em;
	
	public void insert(Betitem betitem) {
		em.persist(betitem);
	}
	public Betitem update(Betitem betitem) {
		return em.merge(betitem);
	}
	public void delete(Betitem betitem) {
		em.remove(em.merge(betitem));
	}
	public Betitem get(Object id) {
		return em.find(Betitem.class, id);
	}
	public List<Betitem> getAllBetitems(Bet bet){
		List<Betitem> betitems = new ArrayList<>();
		Betitem b = null;
	    Query query = em.createQuery("select b from Betitem b where b.bet=:bet");
	    query.setParameter("bet", bet);
	    
	    try{
	        betitems= (List<Betitem>) query.getResultList();
	        System.out.println("betItemsList: "+ query.getResultList());
	        return betitems;
	    }catch (Exception e){
	    	
	        return null;
	    }
	    
	}
}
