package com.buk.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the event database table.
 * 
 */
@Entity
@NamedQuery(name="Event.findAll", query="SELECT e FROM Event e")
public class Event implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private float rate0;

	private float ratea;

	private float rateb;
	
	private int scorea;
	
	private int scoreb;

	private String result;
	
	private String status;

	private String teama;

	private String teamb;
	
	private LocalDateTime matchDate;
	
	private int matchID;

	//bi-directional many-to-one association to Betitem
	@OneToMany(mappedBy="event")
	private List<Betitem> betitems;

	public Event() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getRate0() {
		return this.rate0;
	}

	public void setRate0(float rate0) {
		this.rate0 = rate0;
	}

	public float getRatea() {
		return this.ratea;
	}

	public void setRatea(float ratea) {
		this.ratea = ratea;
	}

	public float getRateb() {
		return this.rateb;
	}

	public void setRateb(float rateb) {
		this.rateb = rateb;
	}

	public String getResult() {
		return this.result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	public int getScorea() {
		return this.scorea;
	}
	public void setScorea(int scorea) {
		this.scorea = scorea;
	}
	public int getScoreb() {
		return this.scoreb;
	}
	public void setScoreb(int scoreb) {
		this.scoreb = scoreb;
	}

	public String getTeama() {
		return this.teama;
	}

	public void setTeama(String teama) {
		this.teama = teama;
	}

	public String getTeamb() {
		return this.teamb;
	}

	public void setTeamb(String teamb) {
		this.teamb = teamb;
	}
	public LocalDateTime getMatchDate() {
		return matchDate;
	}
	public void setMatchDate(LocalDateTime matchDate) {
		this.matchDate = matchDate;
	}
	
	public int getMatchID() {
		return this.matchID;
	}
	public void setMatchID(int matchID) {
		this.matchID = matchID;
	}

	public List<Betitem> getBetitems() {
		return this.betitems;
	}

	public void setBetitems(List<Betitem> betitems) {
		this.betitems = betitems;
	}

	public Betitem addBetitem(Betitem betitem) {
		getBetitems().add(betitem);
		betitem.setEvent(this);

		return betitem;
	}

	public Betitem removeBetitem(Betitem betitem) {
		getBetitems().remove(betitem);
		betitem.setEvent(null);

		return betitem;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}