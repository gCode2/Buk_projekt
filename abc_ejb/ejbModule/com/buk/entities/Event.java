package com.buk.entities;

import java.io.Serializable;
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

	private int rate0;

	private int ratea;

	private int rateb;

	private String result;

	private int teama;

	private int teamb;

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

	public int getRate0() {
		return this.rate0;
	}

	public void setRate0(int rate0) {
		this.rate0 = rate0;
	}

	public int getRatea() {
		return this.ratea;
	}

	public void setRatea(int ratea) {
		this.ratea = ratea;
	}

	public int getRateb() {
		return this.rateb;
	}

	public void setRateb(int rateb) {
		this.rateb = rateb;
	}

	public String getResult() {
		return this.result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public int getTeama() {
		return this.teama;
	}

	public void setTeama(int teama) {
		this.teama = teama;
	}

	public int getTeamb() {
		return this.teamb;
	}

	public void setTeamb(int teamb) {
		this.teamb = teamb;
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

}