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

	private float amount;

	private float betContribution;

	private float betValue;

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

	public float getAmount() {
		return this.amount;
	}

	public void setAmount(float newAmount) {
		this.amount = newAmount;
	}

	public float getBetContribution() {
		return this.betContribution;
	}

	public void setBetContribution(float newContribution) {
		this.betContribution = newContribution;
	}

	public float getBetValue() {
		return this.betValue;
	}

	public void setBetValue(float betValue) {
		this.betValue = betValue;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}