package com.buk.dao;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

@Named
@RequestScoped

public class IndexDAO {
	
	
	public String indexPage() {
		return "/public/index";
	}
}
