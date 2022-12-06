package com.buk.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the user database table.
 * 
 */
@Entity
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private String email;

	private String name;

	private String surname;

	//bi-directional many-to-one association to Bet
	@OneToMany(mappedBy="user")
	private List<Bet> bets;

	//bi-directional many-to-many association to Address
	@ManyToMany(mappedBy="users")
	private List<Address> addresses;

	//bi-directional many-to-one association to Wallet
	@OneToMany(mappedBy="user")
	private List<Wallet> wallets;

	public User() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return this.surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public List<Bet> getBets() {
		return this.bets;
	}

	public void setBets(List<Bet> bets) {
		this.bets = bets;
	}

	public Bet addBet(Bet bet) {
		getBets().add(bet);
		bet.setUser(this);

		return bet;
	}

	public Bet removeBet(Bet bet) {
		getBets().remove(bet);
		bet.setUser(null);

		return bet;
	}

	public List<Address> getAddresses() {
		return this.addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}

	public List<Wallet> getWallets() {
		return this.wallets;
	}

	public void setWallets(List<Wallet> wallets) {
		this.wallets = wallets;
	}

	public Wallet addWallet(Wallet wallet) {
		getWallets().add(wallet);
		wallet.setUser(this);

		return wallet;
	}

	public Wallet removeWallet(Wallet wallet) {
		getWallets().remove(wallet);
		wallet.setUser(null);

		return wallet;
	}

}