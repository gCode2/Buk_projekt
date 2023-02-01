package buk_web;

import java.io.IOException;
import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import com.buk.dao.EventDAO;
import com.buk.dao.WalletDAO;
import com.buk.entities.Wallet;

@Named("walletBB")
@ViewScoped
public class WalletBB implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	EventDAO eventDAO;
	@Inject
	WalletDAO walletDAO;
	
	private float amount;
	private float betContribution;
	private float betAmount;
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public float getBetContribution() {
		return betContribution;
	}
	public void setBetContribution(float betContribution) {
		this.betContribution = betContribution;
	}
	public float getBetAmount() {
		return betAmount;
	}
	public void setBetAmount(float betAmount) {
		this.betAmount = betAmount;
	}
	
	public void walletPage(int uID) throws IOException {
		FacesContext ctx = FacesContext.getCurrentInstance();
		Wallet uWallet = walletDAO.getWalletFromDatabase(uID);
		
		HttpSession session = (HttpSession) ctx.getExternalContext().getSession(false);
		session.setAttribute("uWallet", uWallet);
		
		
		ctx.getExternalContext().redirect("wallet.xhtml");
	}
	public String freeBukTokens(int uID, float amount) {
		FacesContext ctx = FacesContext.getCurrentInstance();
		
		Wallet uWallet = walletDAO.getWalletFromDatabase(uID);
		if(uWallet.getAmount() == 0) {
			walletDAO.addToWallet(uID, amount);
			uWallet = walletDAO.getWalletFromDatabase(uID);
			
			HttpSession session = (HttpSession) ctx.getExternalContext().getSession(false);
			session.setAttribute("uWallet", uWallet);
			return "/pages/buk/wallet?faces-redirect=true";
		}
		else {
			ctx.addMessage("noMoneyForYou", new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Hola hola.. przecież nie straciłeś wszystkiego ;)", null));
			return null;
		}
		
	}
	
	
}
