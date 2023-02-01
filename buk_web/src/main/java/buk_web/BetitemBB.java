package buk_web;

import java.io.Serializable;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.buk.dao.BetitemDAO;
import com.buk.dao.EventDAO;
import com.buk.entities.Bet;
import com.buk.entities.Betitem;
import com.buk.entities.Event;

@Named("betItemBB")
@ViewScoped
public class BetitemBB implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@EJB
	BetitemDAO betitemDAO;
	@EJB
	EventDAO eventDAO;
	
	private String choice;
	private float rate;
	private String status;
	private String teama;
	private String teamb;
	private float totalRate = 1.0f;
	private List<Betitem> betItemList = new ArrayList<>();
	
	private int eventID;
	private Bet bet;
	
//	public BetitemBB() {}
//	public BetitemBB(int event, Bet bet, String choice, float rate, String status, List<Betitem> betItemList, boolean isButtonClicked) {
//		this.setEventID(event);
//		this.setBet(null);
//		this.choice = choice;
//		this.rate = rate;
//		this.status = status;
//		this.betItemList = betItemList;
//		this.isButtonClicked = isButtonClicked;
//	}
	public String getChoice() {
		return choice;
	}
	public void setChoice(String choice) {
		this.choice = choice;
	}
	public float getRate() {
		return rate;
	}
	public void setRate(float rate) {
		this.rate = rate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTeama() {
		return teama;
	}
	public void setTeama(String teama) {
		this.teama = teama;
	}
	public String getTeamb() {
		return teamb;
	}
	public void setTeamb(String teamb) {
		this.teamb = teamb;
	}
	public List<Betitem> getBetItemList() {
	    return betItemList;
	}
	public void setBetItemList(List<Betitem> betItemList) {
	    this.betItemList = betItemList;
	}
	public int getEventID() {
		return eventID;
	}
	public void setEventID(int eventID) {
		this.eventID = eventID;
	}
	public Bet getBet() {
		return bet;
	}
	public void setBet(Bet bet) {
		this.bet = bet;
	}
	public float getTotalRate() {
		return totalRate;
	}
	public void setTotalRate(float totalRate) {
		this.totalRate = totalRate;
	}
	public void addToBet(int event, String choice, float rate, String status) {
		//SPRAWDZ CZY ITEM JEST JUZ NA LISCIE
		Boolean eventExists = false;
		for(int i = 0; i < betItemList.size(); i++) {
			Betitem currItem = betItemList.get(i);
			if(currItem.getEvent().getMatchID() == event) {
				eventExists = true;
				break;
			}
		}
		//JESLI NIE - DODAJ (UNIEMOZLIWIA DODANIE TEGO SAMEGO BETITEMU KILKA RAZY)
		if (!eventExists) {
			Betitem betitem = new Betitem();
			betitem.setEvent(eventDAO.checkEvent(event));
			betitem.setRate(rate);
			betitem.setChoice(choice);
			betitem.setStatus(status);

			betItemList.add(betitem);

			setTotalRate(totalRate(betItemList));
		}
	}
	
	public void removeFromBet(int evID) {
	    for (int i = 0; i < betItemList.size(); i++) {
	    	System.out.println(betItemList.get(i).getEvent().getId());
	        if (betItemList.get(i).getEvent().getId() == evID) {
	            betItemList.remove(i);

	        }
	    }
	    setBetItemList(new ArrayList<>(betItemList));
	    setTotalRate(totalRate(betItemList));
	}
	
	public float totalRate(List<Betitem> betItemRates) {
	    float total = 1.0f;
	    for(Betitem betitem : betItemRates) {
	        total *= betitem.getRate();
	    }
	    return (float) Math.round(total * 100) / 100;
	}
}
