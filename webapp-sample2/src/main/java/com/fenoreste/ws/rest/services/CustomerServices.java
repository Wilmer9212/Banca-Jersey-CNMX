package com.fenoreste.ws.rest.services;

import com.fenoreste.ws.rest.Bankingly.dto.*;
import com.fenoreste.ws.rest.dao.CustomerDAO;
import com.fenoreste.ws.rest.modelos.*;
import java.math.BigDecimal;

import java.util.List;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

@Path("/customer")
public class CustomerServices {
    //Desde Linux
    @POST
    @Path("GetClientsByDocuments")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response getClientsByDocument(String cadenaJson) throws JSONException, Throwable {
        System.out.println("Cadena Json:" + cadenaJson);
        JSONObject jsonRecibido = new JSONObject(cadenaJson);
        JsonObject Json_De_Error = null;
        JsonObject jsonServido=null;
        String DocumentId = jsonRecibido.getString("documentId");
        int ClientType = jsonRecibido.getInt("clientType");
        String Name = jsonRecibido.getString("name");
        String LastName = jsonRecibido.getString("lastName");
        String Mail = jsonRecibido.getString("mail");
        String Phone = jsonRecibido.getString("phone");
        String CellPhone = jsonRecibido.getString("cellphone");
        String UserName = jsonRecibido.getString("userName");
        usuarios_banca_bankingly usersWeb = new usuarios_banca_bankingly();
        CustomerDAO metodos = new CustomerDAO();
        boolean bande = metodos.findUser(UserName);
        boolean buscaP = metodos.SearchPersonas(Name, LastName, Mail, Phone, CellPhone, UserName);
        System.out.println("Persona:" + buscaP);
        try {
            if (buscaP) {
                if (bande == false) {
                    Json_De_Error=Json.createObjectBuilder().add("type", "urn:vn:error-codes:VAL00003").add("title", "USUARIO YA SE ENCUENTRA REGISTRADO").build();
                           
                    return Response.status(Response.Status.BAD_REQUEST).entity(Json_De_Error).build();
                } else {
                    GetClientByDocumentDTO clientes = null;
                    clientes = metodos.getClientByDocument(DocumentId, ClientType, Name, LastName, Mail, Phone, CellPhone, UserName);
                    if (clientes != null) {
                        javax.json.JsonArrayBuilder jsona=Json.createArrayBuilder();
                        javax.json.JsonObjectBuilder jsons=Json.createObjectBuilder(); 
                        com.github.cliftonlabs.json_simple.JsonObject jsono= new com.github.cliftonlabs.json_simple.JsonObject();
                        
                        GetClientByDocumentDTO cliente=null;
                        JsonObjectBuilder data = Json.createObjectBuilder();
                        JsonObject clientess =            data
                                                        .add("ClientBankIdentifier",clientes.getClientBankIdentifier())
                                                        .add("ClientName",clientes.getClientName())
                                                        .add("clientType",clientes.getClientType())
                                                        .add("documentId", clientes.getDocumentId()).build();
                        jsona.add(clientess);
                        JsonObject customersJson = jsons.add("customers",jsona).build();
                        return Response.status(Response.Status.OK).entity(customersJson).build();
                    } else {
                        Json_De_Error=Json.createObjectBuilder().add("type", "urn:vn:error-codes:VAL00003").add("title", "ERROR SOCIO ASIGNADO A USUARIO").build();
                                
                   return Response.status(Response.Status.BAD_REQUEST).entity(Json_De_Error).build();
                    }

                }
            } else {
                Json_De_Error=Json.createObjectBuilder().add("type", "urn:vn:error-codes:VAL00003").add("title", "SOCIO NO EXISTE,VERIFIQUE DATOS").build();
                
                return Response.status(Response.Status.BAD_REQUEST).entity(Json_De_Error).build();
            }
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
            return null;
        } finally {
            metodos.cerrar();
        }

    }
    

     //Estos lo puse desde windows
    @GET
    @Path("/tests")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response pruebas() {
        CustomerDAO datos = new CustomerDAO();
        String nombre = datos.nombre();
        javax.json.JsonObject jsoni=null;
        org.json.JSONObject jsonol=new org.json.JSONObject();
        jsonol.put("hola",nombre);
        com.github.cliftonlabs.json_simple.JsonObject jsono= new com.github.cliftonlabs.json_simple.JsonObject();
        org.json.simple.JSONObject jsonko=new org.json.simple.JSONObject();
        jsonko.put("Hola amigo:",nombre);
        
        jsoni=Json.createObjectBuilder().add("HOLA:",nombre).build();
	
            datos.cerrar();
        

        return Response.status(Response.Status.CREATED).entity(jsonol).build();
    }

    @GET
    @Path("/testing")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response pruebas1() {
        javax.json.JsonArrayBuilder jsona=Json.createArrayBuilder();
                        javax.json.JsonObjectBuilder jsons=Json.createObjectBuilder();
                        
                        GetClientByDocumentDTO cliente=null;
                        for(int i=0;i<2;i++){
                            jsons.add("","hola").add("mundo","hola").build();
                                    jsona.add(jsons);
                        }
                        return Response.status(Response.Status.OK).entity(jsona).build();
    }

}
