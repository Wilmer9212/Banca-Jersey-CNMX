/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fenoreste.rest.services;

import com.fenoreste.rest.ResponseDTO.LoanDTO;
import com.fenoreste.rest.dao.LoanDAO;
import com.github.cliftonlabs.json_simple.JsonObject;
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
@Path("/Loan")
public class LoanResources {

    @POST
    @Produces({MediaType.APPLICATION_JSON + ";charset=utf-8"})
    @Consumes({MediaType.APPLICATION_JSON + ";charset=utf-8"})
    public Response loan(String cadena) {
        System.out.println("Cadena:" + cadena);
        String productBankIdentifier = "";
        JsonObject Error = new JsonObject();
        int feesStatus = 0, pageSize = 0, pageStartIndex = 0;
        try {
            JSONObject jsonRecibido = new JSONObject(cadena);
            productBankIdentifier = jsonRecibido.getString("productBankIdentifier");
            feesStatus = jsonRecibido.getInt("feesStatus");
            JSONObject json = jsonRecibido.getJSONObject("paging");
            pageSize = json.getInt("pageSize");
            pageStartIndex = json.getInt("pageStartIndex");
        } catch (Exception e) {
            Error.put("Error", "Error en parametros JSON");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Error).build();
        }
        LoanDAO dao=new LoanDAO();
        int count = 0;
        try {
            LoanDTO loan = dao.Loan(productBankIdentifier, feesStatus, pageSize, pageStartIndex);
            JsonObject j = new JsonObject();
            if (count > 0) {
                j.put("MovementsCount", count);
            } else {
                
                return Response.status(Response.Status.NO_CONTENT).entity(Error).build();
            }

            return Response.status(Response.Status.OK).entity(j).build();
        } catch (Exception e) {
            dao.cerrar();
            Error.put("Error", "SOCIOS NO ENCONTRADOS");
            System.out.println("Error al convertir cadena a JSON:" + e.getMessage());
            return Response.status(Response.Status.NO_CONTENT).entity(Error).build();

        } finally {
            dao.cerrar();
        }

        
        
    }
}
