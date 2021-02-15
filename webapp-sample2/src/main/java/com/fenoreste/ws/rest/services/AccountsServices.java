package com.fenoreste.ws.rest.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONObject;

import com.fenoreste.ws.rest.Bankingly.dto.GetAccountDetailsDTO;
import com.fenoreste.ws.rest.Bankingly.dto.GetAccountLast5MovementsDTO;
import com.fenoreste.ws.rest.Bankingly.dto.GetAccountMovementsDTO;
import com.fenoreste.ws.rest.dao.AccountsDAO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import org.codehaus.jettison.json.JSONException;

@Path("/accounts")
public class AccountsServices {

    @POST
    @Path("/GetAccountDetails")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response getAccountDetails(String cadenaJson) {
        System.out.println("Cadena Json:" + cadenaJson);
        String accountId = "";
        try {
            JSONObject jsonRecibido = new JSONObject(cadenaJson);
            accountId = jsonRecibido.getString("productBankIdentifier");
        } catch (Exception e) {
            System.out.println("Error al convertir cadena a JSON:" + e.getMessage());
        }

        JsonObject Json_De_Error = null;

        boolean bande = true;
        //Reccorremos el accountId para veru que solo sean numeros que trae
        for (int i = 0; i < accountId.length(); i++) {
            if (Character.isLetter(accountId.charAt(i))) {
                bande = false;
                System.out.println("Charat:" + accountId.charAt(i));
            }
        }

        System.out.println("Bande:" + bande);
        AccountsDAO metodos = new AccountsDAO();

        //Si no trae letras en Identificador de producto(OPA) y la longitud es igual a lo que se maneja en la caja 
        if (bande == true && accountId.length() == 19) {
            GetAccountDetailsDTO cuenta = null;
            try {
                cuenta = metodos.GetAccountDetails(accountId);
                if (cuenta != null) {
                    return Response.status(Response.Status.OK).entity(cuenta).build();
                } else {
                    Json_De_Error = Json.createObjectBuilder().add("type", "urn:vn:error-codes:VAL00003").add("title", "ERROR PRODUCTO NO ENCONTRADO").build();
                    metodos.cerrar();
                    return Response.status(Response.Status.BAD_REQUEST).entity(Json_De_Error).build();
                }
            } catch (Exception e) {
                Json_De_Error = Json.createObjectBuilder().add("type", "urn:vn:error-codes:VAL00003").add("title", "ERROR INTERNO EN EL SERVIDOR").build();
                metodos.cerrar();
                return Response.status(Response.Status.BAD_REQUEST).entity(Json_De_Error).build();
            }

        } else {
            Json_De_Error = Json.createObjectBuilder().add("type", "urn:vn:error-codes:VAL00003").add("title", "FORMATO DE INDETIFICADOR INVALIDO").build();
            metodos.cerrar();
            return Response.status(Response.Status.BAD_REQUEST).entity(Json_De_Error).build();
        }

    }

    @POST
    @Path("/GetAccountMovements")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response getAccountMovements(String cadenaJson) {
        AccountsDAO dao = new AccountsDAO();
        String ProductBankIdentifier = "";
        String DateFromFilter = null;
        String DateToFilter = null;
        int PageSize = 0;
        int PageStartIndex = 0;

        try {
            JSONObject jsonRecibido = new JSONObject(cadenaJson);
            ProductBankIdentifier = jsonRecibido.getString("productBankIdentifier");
            DateFromFilter = jsonRecibido.getString("dateFromFilter");
            DateToFilter = jsonRecibido.getString("dateToFilter");
            JSONObject json = jsonRecibido.getJSONObject("paging");
            PageSize = json.getInt("pageSize");
            PageStartIndex = json.getInt("pageStartIndex");
            List<GetAccountMovementsDTO>MiListaDTO=dao.getAccountMovements(ProductBankIdentifier, DateFromFilter, DateToFilter, PageSize, PageStartIndex);
            
        } catch (Exception e) {
            System.out.println("Error al convertir cadena a JSON:" + e.getMessage());

        } finally {
            dao.cerrar();
        }
        System.out.println("ProductBankIdentifier:" + ProductBankIdentifier);
        System.out.println("DateFromFilter:" + DateFromFilter);
        System.out.println("DateToFilter:" + DateToFilter);
        System.out.println("PageSize:" + PageSize);
        System.out.println("PageStartIndex:" + PageStartIndex);
        //dao.getAccountMovements(cadenaJson, cadenaJson, cadenaJson)
        return Response.status(Response.Status.CREATED).build();
    }

    @POST
    @Path("/GetAccountLast5Movements")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response getAccountLast5Movements(String cadenaJson) throws JSONException, Throwable {
        System.out.println("Cadena Json:" + cadenaJson);
        JSONObject jsonRecibido = new JSONObject(cadenaJson);
        JsonObject Json_De_Error = null;
        JsonObjectBuilder ObjectBuilder = Json.createObjectBuilder();
        JsonArrayBuilder arrayEsqueleto = Json.createArrayBuilder();
        String accountId = jsonRecibido.getString("productBankIdentifier");
        boolean bandera = true;
        for (int i = 0; i < accountId.length(); i++) {
            if (Character.isLetter(accountId.charAt(i))) {
                bandera = false;
            }
        }
        AccountsDAO metodos = new AccountsDAO();
        List<GetAccountLast5MovementsDTO> cuenta = new ArrayList<GetAccountLast5MovementsDTO>();
        GetAccountLast5MovementsDTO cuentaM = null;
        if (bandera) {
            try {
                cuenta = metodos.getAccountLast5Movements(accountId);
                if (cuenta.size() > 0) {
                    metodos.cerrar();

                    for (int i = 0; i < cuenta.size(); i++) {
                        cuentaM = cuenta.get(i);
                        System.out.println("Movimientos:" + cuenta);
                        JsonObjectBuilder data = Json.createObjectBuilder();
                        JsonObject clientes = data.add("MovementId", cuentaM.getMovementId())
                                .add("ccountBankIdentifier", cuentaM.getAccountBankIdentifier())
                                .add("MovementDate", cuentaM.getMovementDate())
                                .add("Description", cuentaM.getDescription())
                                .add("Amount", cuentaM.getAmount())
                                .add("isDebit", cuentaM.isIsDebit())
                                .add("Balance", cuentaM.getBalance())
                                .add("MovementTypeId", cuentaM.getMovementId())
                                .add("TypeDescription", cuentaM.getTypeDescription())
                                .add("CheckId", cuentaM.getCheckId())
                                .add("VoucherId", cuentaM.getVoucherId()).build();

                        arrayEsqueleto.add(clientes);
                    }
                    JsonObject cuentasJson = ObjectBuilder.add("AccountLast5Movements", arrayEsqueleto).build();
                    return Response.status(Response.Status.OK).entity(cuentasJson).build();
                } else {
                    Json_De_Error = Json.createObjectBuilder().add("type", "urn:vn:error-codes:VAL00003").add("title", "PRODUCTO NO ENCONTRADO").build();
                    metodos.cerrar();
                    return Response.status(Response.Status.BAD_REQUEST).entity(Json_De_Error).build();
                }

            } catch (Exception e) {
                Json_De_Error = Json.createObjectBuilder().add("type", "urn:vn:error-codes:VAL00003").add("title", "ERROR INTERNO EN EL SERVIDOR").build();
                metodos.cerrar();
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Json_De_Error).build();
            }

        } else {
            Json_De_Error = Json.createObjectBuilder().add("type", "urn:vn:error-codes:VAL00003").add("title", "CARACTERES INVALIDOS EN ENTRADA").build();
            metodos.cerrar();
            return Response.status(Response.Status.BAD_REQUEST).entity(Json_De_Error).build();
        }

    }

}
