package com.fenoreste.ws.rest.services;

import com.fenoreste.ws.rest.Bankingly.dto.*;
import com.fenoreste.ws.rest.dao.CustomerDAO;
import com.fenoreste.ws.rest.modelos.*;
import com.github.cliftonlabs.json_simple.JsonObject;

import java.util.List;
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

    @POST
    @Path("GetClientsByDocuments")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response getClientsByDocument(String cadenaJson) throws JSONException, Throwable {
        System.out.println("Cadena Json:" + cadenaJson);
        JSONObject jsonRecibido = new JSONObject(cadenaJson);
        JsonObject Json_De_Error = new JsonObject();
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
                    Json_De_Error.put("type", "urn:vn:error-codes:VAL00003");
                    Json_De_Error.put("title", "USUARIO YA SE ENCUENTRA REGISTRADO");
                           
                    return Response.status(Response.Status.BAD_REQUEST).entity(Json_De_Error).build();
                } else {
                    List<GetClientByDocumentDTO> clientes = null;
                    clientes = metodos.getClientByDocument(DocumentId, ClientType, Name, LastName, Mail, Phone, CellPhone, UserName);
                    if (clientes != null) {
                        return Response.status(Response.Status.OK).entity(clientes).build();
                    } else {
                        Json_De_Error.put("type", "urn:vn:error-codes:VAL00003");
                        Json_De_Error.put("title", "ERROR SOCIO ASIGNADO A USUARIO");
                                
                        return Response.status(Response.Status.BAD_REQUEST).entity(Json_De_Error).build();
                    }

                }
            } else {
                Json_De_Error.put("type", "urn:vn:error-codes:VAL00003");
                Json_De_Error.put("title", "SOCIO NO EXISTE,VERIFIQUE DATOS");
                
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
        JsonObject json=new JsonObject();
		json.put("hola",nombre);
	
            datos.cerrar();
        

        return Response.status(Response.Status.CREATED).entity(json).build();
    }

    @GET
    @Path("/testing")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response pruebas1() {
        return Response.status(Response.Status.CREATED).entity("Hola mundo").build();
    }

}
