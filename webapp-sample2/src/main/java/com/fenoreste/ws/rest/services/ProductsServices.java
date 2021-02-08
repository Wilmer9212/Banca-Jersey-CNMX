/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fenoreste.ws.rest.services;

import com.fenoreste.ws.rest.Bankingly.dto.GetAccountDetailsDTO;
import com.fenoreste.ws.rest.dao.ProductsDAO;
import com.github.cliftonlabs.json_simple.JsonObject;
import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author wilmer
 */
@Path("/products")
public class ProductsServices {
    
    
    @POST
    @Path("/GetProducts")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response ObtenerProductos(String cadena) throws Throwable { 
        ProductsDAO prodDao=new ProductsDAO();
        
        JsonObjectBuilder ObjectBuilder = Json.createObjectBuilder();
        JsonArrayBuilder arrayEsqueleto = Json.createArrayBuilder();        
        JsonObject JsonSocios = new JsonObject();        
        JsonObject Not_Found = new JsonObject() ;
        System.out.println("cadena:" + cadena);

        JSONObject mainObject = new JSONObject(cadena);
        System.out.println("JsonObjetc:"+mainObject);
        String productBankIdentifier = "";
        String productType = "";
        try {
        
        for (int i = 0; i < mainObject.length(); i++) {
            String prodBI = mainObject.getString("productBankIdentifier");
            String prodTp = mainObject.getString("productsType");
            JSONArray BI = new JSONArray(prodBI);
            JSONArray PT = new JSONArray(prodTp);
            for (int x = 0; x < BI.length(); x++) {
                JSONObject jsonO = new JSONObject(BI.getJSONObject(x).toString());
                productBankIdentifier = jsonO.getString("value");
                System.out.println("productBankIdentifier:"+productBankIdentifier);
            }
            
            for (int x = 0; x < PT.length(); x++) {
                JSONObject jsonO = new JSONObject(PT.getJSONObject(x).toString());
                productType = jsonO.getString("value");
                System.out.println("productType:"+productType);
            }
            
            List<GetAccountDetailsDTO>ListaA=new ArrayList<GetAccountDetailsDTO>();
            
            prodDao.getProductos(productBankIdentifier,productType);
            
        }
        } catch (Exception e) {
        }finally{
            prodDao.cerrar();
        }
        return null;

        
    }

    
}
