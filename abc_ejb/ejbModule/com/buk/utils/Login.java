package com.buk.utils;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import javax.faces.bean.ManagedBean;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;


import com.buk.dao.LoginDAO;
import com.buk.utils.SessionUtils;

@Named
@ManagedBean
@SessionScoped
public class Login {
	private String user;
	private String pwd;
	private String msg;
	

	@Inject
	FacesContext ctx;
	
	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	//validate login
		public String validateUsernamePassword() {
			boolean valid = LoginDAO.validate(user, pwd);
			if (valid) {
				HttpSession session = SessionUtils.getSession();
				session.setAttribute("username", user);
				return "admin";
			} else {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_WARN,
								"Incorrect Username and Passowrd",
								"Please enter correct username and Password"));
				return "login";
			}
		}

		//logout event, invalidate session
		public String logout() {
			HttpSession session = SessionUtils.getSession();
			session.invalidate();
			return "login";
		}
}
