package com.fenoreste.ws.rest.services;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.transaction.Transaction;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fenoreste.ws.rest.Util.AbstractFacade;
import com.fenoreste.ws.rest.Util.JPAUtil;


@Path("/delete")
public class Services {
        //Se esta modificando nueva modificacion
	@GET
	@Path("/users")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	public Response test() {
		EntityManagerFactory emf=AbstractFacade.conexion();
		EntityManager em=emf.createEntityManager();
		EntityTransaction tr;
		tr=em.getTransaction();
		tr.begin();
		int ree=em.createNativeQuery("DELETE FROM usuarios_bancam_bankingly").executeUpdate();
		System.out.println("Registros Eliminados:"+ree);
		emf.close();
		return Response.ok("Registros eliminados:"+ree).build();
	}
}
