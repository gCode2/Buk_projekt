package buk_web;


import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.simplesecurity.RemoteClient;
import javax.faces.view.ViewScoped;
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
public class RegisterBB implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	UserDAO userDAO;
	@Inject
	WalletDAO walletDAO;
	
	private static final String PAGE_MAIN = "/pages/login.jsf";
    private static final String PAGE_STAY_AT_THE_SAME = null;

    private String email;
    private String name;
    private String surname;
    private String pass;
    private String passq;
    
    
    public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}
    
	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}
	
	public String getPassq() {
		return passq;
	}

	public void setPassq(String passq) {
		this.passq = passq;
	}
    
	public String registerPage() {
		return "/pages/register?faces-redirect=true";
	}
	
	public String doRegister(){
		
		FacesContext ctx = FacesContext.getCurrentInstance();
		
        if (!userDAO.checkLogin(this.email)){
            ctx.addMessage("email", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Login juz zajęty", null));
            return PAGE_STAY_AT_THE_SAME;
        }
        if (!pass.equals(passq)){
            ctx.addMessage("password2", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Hasła nie są równe", null));
            return PAGE_STAY_AT_THE_SAME;
        }

        User user = new User();
        Wallet wallet = new Wallet();

        //utworzenie usera
        user.setName(name);
        user.setSurname(surname);
        user.setEmail(email);
        user.setPassword(pass);
        user.setRole("user");
        
        userDAO.insert(user);
        
        //utworzenie portfela dla nowego uzytkownika
        
        wallet.setAmount(100);
        wallet.setBetContribution(0);
        wallet.setBetValue(0);
        wallet.setUser(user);
        
        walletDAO.insert(wallet);
        
        RemoteClient<User> client = new RemoteClient<User>(); //create new RemoteClient
        client.setDetails(user);


        //store RemoteClient with request info in session (needed for SecurityFilter)
        HttpServletRequest request = (HttpServletRequest) ctx.getExternalContext().getRequest();
        client.store(request);
        
        ctx.addMessage("creationConfirmation", new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Poprawnie utworzono konto", null));
		return PAGE_MAIN;
		
    }
	
}

