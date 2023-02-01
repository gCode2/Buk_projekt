package com.buk.entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.ws.rs.ext.ParamConverter.Lazy;

import java.util.List;


/**
 * The persistent class for the bet database table.
 * 
 */
@Entity
@Lazy
@NamedQuery(name="Bet.findAll", query="SELECT b FROM Bet b")
public class Bet implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private float contribution;

	private float rate;

	private String status;

	private float value;
	
	private boolean isAvailable;
	
	private boolean isReceived;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="userID")
	private User user;

	//bi-directional many-to-one association to Betitem

	@OneToMany(mappedBy="bet")
	private List<Betitem> betitems;

	public Bet() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getContribution() {
		return this.contribution;
	}

	public void setContribution(float contribution) {
		this.contribution = contribution;
	}

	public float getRate() {
		return this.rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public float getValue() {
		return this.value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	public List<Betitem> getBetitems() {
		return this.betitems;
	}

	public void setBetitems(List<Betitem> betitems) {
		this.betitems = betitems;
	}
	
	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public boolean isReceived() {
		return isReceived;
	}

	public void setReceived(boolean isReceived) {
		this.isReceived = isReceived;
	}

	public Betitem addBetitem(Betitem betitem) {
		getBetitems().add(betitem);
		betitem.setBet(this);

		return betitem;
	}

	public Betitem removeBetitem(Betitem betitem) {
		getBetitems().remove(betitem);
		betitem.setBet(null);

		return betitem;
	}

	

	
	

}