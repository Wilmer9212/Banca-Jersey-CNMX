package com.fenoreste.ws.rest.dao;

import com.fenoreste.ws.rest.modelos.Persona;

public class CustomerDAO extends FacadeCustomer<Persona> {

	 public CustomerDAO() {
	     super(Persona.class);
  }     
}
