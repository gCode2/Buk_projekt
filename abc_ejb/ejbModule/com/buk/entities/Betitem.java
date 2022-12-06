package com.buk.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the betitem database table.
 * 
 */
@Entity
@NamedQuery(name="Betitem.findAll", query="SELECT b FROM Betitem b")
public class Betitem implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private String choice;

	private float rate;

	private String status;

	//bi-directional many-to-one association to Bet
	@ManyToOne
	@JoinColumn(name="betID")
	private Bet bet;

	//bi-directional many-to-one association to Event
	@ManyToOne
	@JoinColumn(name="eventID")
	private Event event;

	public Betitem() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getChoice() {
		return this.choice;
	}

	public void setChoice(String choice) {
		this.choice = choice;
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

	public Bet getBet() {
		return this.bet;
	}

	public void setBet(Bet bet) {
		this.bet = bet;
	}

	public Event getEvent() {
		return this.event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

}