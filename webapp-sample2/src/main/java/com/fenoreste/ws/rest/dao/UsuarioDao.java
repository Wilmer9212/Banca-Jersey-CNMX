package com.fenoreste.ws.rest.dao;

public class UsuarioDao {

	
	  public static boolean validar(String username,String password) {
		  
		  return (username.equals("admin") && password.equals("admin"));
	  }
	  
	
	     
}
