package com.fenoreste.ws.rest.dao;

import com.fenoreste.ws.rest.modelos.Productos;
public class ProductsDAO extends FacadeProductos<Productos> {
	 public ProductsDAO() {
		    super(Productos.class);
      }     
}
