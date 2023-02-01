package buk_web;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import com.buk.dao.BetDAO;
import com.buk.dao.BetitemDAO;
import com.buk.dao.EventDAO;
import com.buk.dao.WalletDAO;
import com.buk.entities.Bet;
import com.buk.entities.Betitem;
import com.buk.entities.Event;
import com.buk.entities.User;
import com.buk.entities.Wallet;

@Named("betBB")
@RequestScoped
public class BetBB implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Inject
	BetDAO betDAO;
	@Inject
	BetitemDAO betItemDAO;
	@Inject
	WalletDAO walletDAO;
	@Inject
	EventDAO eventDAO;
	@Inject
	WalletBB walletBB;
	
	
	private float rate;
	private float contribution;
	private float value;
	private String status;
	private boolean isAvailable;
	private boolean isReceived;
	private User user;
	private List<Betitem> matches;
	private List<Bet> bets;
	
	
	public String matchListPage() {
		return "/pages/buk/index?faces-redirect=true";
	}
	
	public float getRate() {
		return rate;
	}
	public void setRate(float rate) {
		this.rate = rate;
	}
	public float getContribution() {
		return contribution;
	}
	public void setContribution(float contribution) {
		this.contribution = contribution;
	}
	public float getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public List<Betitem> getMatches() {
		return matches;
	}
	public void setMatches(List<Betitem> matches) {
		this.matches = matches;
	}
	
	public List<Bet> getBets() {
		return bets;
	}
	public void setBets(List<Bet> bets) {
		this.bets = bets;
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
	public void betPage(User user) throws IOException {
		FacesContext ctx = FacesContext.getCurrentInstance();
		
		List<Bet> bets = betDAO.getAllBets(user);
		Map<Bet, List<Betitem>> betMap = new HashMap<>();
		
		for (Bet bet : bets) {
	        List<Betitem> betitems = betItemDAO.getAllBetitems(bet);
	        List<Betitem> trafione = new ArrayList<>();
	        boolean wTrakcie = false;
	        for(Betitem betitem : betitems) {
	        	String userChoice = betitem.getChoice();
	        	int userMatchID = betitem.getEvent().getMatchID();

	        	Event dbMatch = new Event();
	        	dbMatch = eventDAO.checkEvent(userMatchID);

	        	//MECZ JESZCZE SIE NIE SKONCZYL
	        	if(!dbMatch.getStatus().equals("FINISHED")) {
	        		wTrakcie = true;
	        		continue;
	        	}
	        	//MECZ SIĘ ZAKOŃCZYL
	        	else {
	        		//UZYTKOWNIK ZGADL ZWYCIEZCE
	        		if(dbMatch.getResult().equals(userChoice)) {
	        			trafione.add(betitem);
	        			System.out.println("mID: "+betitem.getEvent().getMatchID());
	        			betitem.setStatus("WESZŁO");
	        		}
	        		//UZYTKOWNIK NIE ZGADL ZWYCIEZCY
	        		else {
	        			System.out.println("NIE WESZLO");
	        		}
	        		betItemDAO.update(betitem);
	        	}
	        	
	        }
	        //JESLI WSZYSTKIE SA TRAFIONE - KUPON WYGRYWA
	        if(trafione.size() == betitems.size()) {
	        	bet.setAvailable(true);
	        	bet.setStatus("WYGRANA");
	        }
	        else if (wTrakcie){
	        	bet.setAvailable(false);
	        	bet.setStatus("W TRAKCIE");
	        }
	        else {
	        	bet.setAvailable(false);
	        	bet.setStatus("PRZEGRANA");
	        }
	        betDAO.update(bet);
	        betMap.put(bet, betitems);
	    }
		
		
		HttpSession session = (HttpSession) ctx.getExternalContext().getSession(false);
		session.setAttribute("bets", bets);
		session.setAttribute("betMap", betMap);
		
		ctx.getExternalContext().redirect("bets.xhtml");
		
	}
	public void placeBet(User user, List<Betitem> matches, float rate, float contribution, float value, String status) throws IOException {
		FacesContext ctx = FacesContext.getCurrentInstance();
		
		Wallet uWallet = walletDAO.getWalletFromDatabase(user.getId());
		float walletAmount = uWallet.getAmount();
		
		//UZYTKOWNIK MUSI POSIADAC SRODKI ORAZ NIE MOZE PODSTAWIC KUPONU ZA 0
		if(walletAmount >= contribution && contribution != 0) {
			//PUSTY KUPON TEZ ODPADA
			if(matches.size() > 0) {
				//ZAOKRAGLANIE DO 2 MIEJSC PO PRZECINKU
				BigDecimal cont = new BigDecimal(contribution);
		        cont = cont.setScale(2, RoundingMode.HALF_UP);
		        float newContribution = cont.floatValue();
				
				BigDecimal val = new BigDecimal(value);
		        val = val.setScale(2, RoundingMode.HALF_UP);
		        float newValue = val.floatValue();
		        Bet userBet = new Bet();
		        
		        //TWORZENIE BETU
				userBet.setUser(user);
				userBet.setRate(rate);
				userBet.setContribution(newContribution);
				userBet.setValue(newValue);
				userBet.setStatus(status);
				userBet.setBetitems(matches);
				userBet.setAvailable(false);
				userBet.setReceived(false);
				walletDAO.extractFromWallet(user.getId(), newContribution);
				
				Wallet uWallet2 = walletDAO.getWalletFromDatabase(user.getId());
				HttpSession session = (HttpSession) ctx.getExternalContext().getSession(false);
				session.setAttribute("uWallet", uWallet2);
				
				
				betDAO.insert(userBet);
				updateBetIdForMatches(userBet, matches);
				betPage(user);
			}
			else {
				ctx.addMessage("betContribution", new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Dodaj mecze do kuponu!", null));
			}
		
		}		
		else {
			ctx.addMessage("betContribution", new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Nieprawidłowa ilość BukTokenów", null));
		}
		
		
		
	}
	//AKTUALIZACJA ID BETU DLA BETITEMOW
	public void updateBetIdForMatches(Bet b, List<Betitem> matches) {
		for (int i = 0; i < matches.size(); i++) {
			  Betitem m = matches.get(i);
			  m.setBet(b);
			  betItemDAO.insert(m);
			}
	}
	//OBSLUGA ZYSKU Z PRZYCISKOW PO WYGRANYCH KUPONACH
	public void receiveBetValue(int uID, float value, Bet bet) throws IOException {
		FacesContext ctx = FacesContext.getCurrentInstance();

		bet.setReceived(true);
		betDAO.update(bet);

		walletDAO.addBetValueToWallet(uID,value);
		Wallet uWallet = walletDAO.getWalletFromDatabase(uID);
		HttpSession session = (HttpSession) ctx.getExternalContext().getSession(false);
		session.setAttribute("uWallet", uWallet);
		
		
		ctx.getExternalContext().redirect("wallet.xhtml");
	}
}
