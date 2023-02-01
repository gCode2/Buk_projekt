package com.buk.dao;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.buk.entities.Wallet;

import javax.persistence.Query;


@Stateless
public class WalletDAO {
	
	@PersistenceContext
	EntityManager em;
	
	
	public void insert(Wallet wallet) {
		em.persist(wallet);
	}
	public Wallet update(Wallet wallet) {
		return em.merge(wallet);
	}
	public void delete(Wallet wallet) {
		em.remove(em.merge(wallet));
	}
	public Wallet get(Object id) {
		return em.find(Wallet.class, id);
	}
	
	public Wallet addToWallet(int userID, float valueToAdd) {
	    Wallet wallet = getWalletFromDatabase(userID);
	    if(wallet != null){
	        float newAmount = wallet.getAmount() + valueToAdd;
	        wallet.setAmount(Math.round(newAmount * 100) / 100);
	        return update(wallet);
	    } else {
	        return null;
	    }
	}
	
	public Wallet addBetValueToWallet(int userID, float valueToAdd) {
		Wallet wallet = getWalletFromDatabase(userID);
	    if(wallet != null){
	        float newAmount = wallet.getAmount() + valueToAdd;
	        float newBetValue = wallet.getBetValue() + valueToAdd;
	        wallet.setAmount(newAmount);
	        wallet.setBetValue(newBetValue);
	        return update(wallet);
	    } else {
	        return null;
	    }
		
	}
	public Wallet extractFromWallet(int userID, float valueToExtract) {
	    Wallet wallet = getWalletFromDatabase(userID);
	    if(wallet != null){
	        float extraction = wallet.getAmount() - valueToExtract;
	        
	        BigDecimal bdValue = new BigDecimal(extraction);
	        bdValue = bdValue.setScale(2, RoundingMode.HALF_UP);
	        float newAmount = bdValue.floatValue();
	        float newContribution = wallet.getBetContribution() + valueToExtract;
	        
	        wallet.setAmount(newAmount);
	        wallet.setBetContribution(newContribution);
	        return update(wallet);
	    } else {
	        return null;
	    }
	}
	public Wallet getWalletFromDatabase(int userID){
	    Query query = em.createQuery("select w from Wallet w where userID =: userID");
	    query.setParameter("userID", userID);
	    try {
	        return (Wallet) query.getSingleResult();
	    } catch (NoResultException e) {
	        return null;
	    }
	}
	
}
