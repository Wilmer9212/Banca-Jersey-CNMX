/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fenoreste.rest.services;

import com.fasterxml.jackson.databind.ser.std.JsonValueSerializer;
import com.fenoreste.rest.ResponseDTO.GetClientByDocumentDTO;
import com.fenoreste.rest.Util.AbstractFacade;
import com.fenoreste.rest.dao.CustomerDAO;
import com.fenoreste.rest.entidades.usuarios_banca_bankingly;
import com.github.cliftonlabs.json_simple.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONObject;

/**
 *
 * @author Elliot
 */
@Path("/Clients")
public class CustomerResources {
   
    @GET    
    @Produces({MediaType.APPLICATION_JSON + ";charset=utf-8"})
    @Consumes({MediaType.APPLICATION_JSON + ";charset=utf-8"})
    public Response test(){
        com.github.cliftonlabs.json_simple.JsonObject j=new com.github.cliftonlabs.json_simple.JsonObject();
        EntityManagerFactory emf=AbstractFacade.conexion();
        EntityManager em=emf.createEntityManager();
        
        Query query=em.createNativeQuery("SELECT nombre FROM personas limit 1");
        String nombre=String.valueOf(query.getSingleResult().toString());
        j.put("nombre",nombre);
        emf.close();
        return Response.ok(j).build();
    }
    
    @POST
    @Path("ByDocuments")
    @Produces({MediaType.APPLICATION_JSON+ ";charset=utf-8"})
    @Consumes({MediaType.APPLICATION_JSON+ ";charset=utf-8"})
    public Response getClientsByDocument(String cadenaJson) throws Throwable{
        System.out.println("Cadena Json:" + cadenaJson);
        JSONObject jsonRecibido = new JSONObject(cadenaJson);
        JsonObject Json_De_Error = new JsonObject();
        JsonObject jsonServido=new JsonObject();
        String DocumentId = jsonRecibido.getString("documentId");
        int ClientType = jsonRecibido.getInt("clientType");
        String Name = jsonRecibido.getString("name");
        String LastName = jsonRecibido.getString("lastName");
        String Mail = jsonRecibido.getString("mail");
        String Phone = jsonRecibido.getString("phone");
        String CellPhone = jsonRecibido.getString("cellPhone");
        String UserName = jsonRecibido.getString("userName");
        usuarios_banca_bankingly usersWeb = new usuarios_banca_bankingly();
        CustomerDAO metodos = new CustomerDAO();
        boolean bande = metodos.findUser(UserName);
        boolean buscaP = metodos.SearchPersonas(Name, LastName, Mail, Phone, CellPhone, UserName);
        System.out.println("Persona:" + buscaP);
        try {
            if (buscaP) {
                /*if (bande == false) {
                    Json_De_Error.put("Error", "USUARIO YA SE ENCUENTRA REGISTRADO");                           
                    return Response.status(Response.Status.BAD_REQUEST).entity(Json_De_Error).build();
                } else {*/
                    GetClientByDocumentDTO clientes = null;
                    clientes = metodos.getClientByDocument(DocumentId, ClientType, Name, LastName, Mail, Phone, CellPhone, UserName);
                    if (clientes != null) {
                      jsonServido.put("customers",clientes);
                        return Response.status(Response.Status.OK).entity(jsonServido).build();
                    } else {
                        Json_De_Error.put("Error", "ERROR SOCIO ASIGNADO A USUARIO");
                   return Response.status(Response.Status.BAD_REQUEST).entity(Json_De_Error).build();
                    }

                //}
            } else {
                Json_De_Error.put("Error", "SOCIO NO EXISTE,VERIFIQUE DATOS");                
                return Response.status(Response.Status.BAD_REQUEST).entity(Json_De_Error).build();
            }
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
            return null;
        } finally {
            metodos.cerrar();
        }
    }  
    
}
