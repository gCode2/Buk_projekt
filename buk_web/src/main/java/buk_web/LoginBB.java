package buk_web;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.simplesecurity.RemoteClient;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.buk.dao.UserDAO;
import com.buk.dao.WalletDAO;
import com.buk.entities.User;
import com.buk.entities.Wallet;

@Named
@RequestScoped
public class LoginBB {
	private static final String PAGE_MAIN = "/pages/buk/index.jsf?faces-redirect=true";
	private static final String PAGE_LOGIN = "/pages/login.jsf?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;

	private String login;
	private String pass;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String loginPage() {
		return "/pages/login?faces-redirect=true";
	}
	
	@Inject
	UserDAO userDAO;
	@Inject
	WalletDAO walletDAO;

	public String doLogin() throws IOException, InterruptedException{
		FacesContext ctx = FacesContext.getCurrentInstance();

		// 1. verify login and password - get User from "database"
		User user = userDAO.getUserFromDatabase(login, pass);
		
		// 2. if bad login or password - stay with error info
		if (user == null) {
			ctx.addMessage("username", new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Niepoprawny login lub hasło", null));
			return PAGE_STAY_AT_THE_SAME;
		}
		// 3. if logged in: get User roles, save in RemoteClient and store it in session
		
		RemoteClient<User> client = new RemoteClient<User>(); //create new RemoteClient
		client.setDetails(user);
		//ZAPISZ DANE PORTFELA DO SESJI
		Wallet uWallet = walletDAO.getWalletFromDatabase(user.getId());
		client.getRoles().add(user.getRole());
		
		//store RemoteClient with request info in session (needed for SecurityFilter)
		HttpServletRequest request = (HttpServletRequest) ctx.getExternalContext().getRequest();
		client.store(request);
		HttpSession session = (HttpSession) ctx.getExternalContext().getSession(false);
		session.setAttribute("uWallet", uWallet);
		return PAGE_MAIN;
	}
	
	
	public String doLogout(){
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		
		FacesContext ctx = FacesContext.getCurrentInstance();
		
		ctx.addMessage("logoutConfirmation", new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Wylogowano pomyślnie", null));
		
		session.invalidate();
		return PAGE_LOGIN;
	}
	
	
}

