package com.buk.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the wallet database table.
 * 
 */
@Entity
@NamedQuery(name="Wallet.findAll", query="SELECT w FROM Wallet w")
public class Wallet implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private int amount;

	private int betContribution;

	private int betValue;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="userID")
	private User user;

	public Wallet() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAmount() {
		return this.amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getBetContribution() {
		return this.betContribution;
	}

	public void setBetContribution(int betContribution) {
		this.betContribution = betContribution;
	}

	public int getBetValue() {
		return this.betValue;
	}

	public void setBetValue(int betValue) {
		this.betValue = betValue;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}