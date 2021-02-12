/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fenoreste.ws.rest.services;
/*
import com.fenoreste.ws.rest.Bankingly.dto.GetAccountDetailsDTO;
import com.fenoreste.ws.rest.dao.ProductsDAO;
import com.github.cliftonlabs.json_simple.JsonObject;
import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
*/
import java.util.List;
import java.util.ArrayList;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONObject;

import com.fenoreste.ws.rest.dao.ProductsDAO;
import com.fenoreste.ws.rest.modelos.entidad.TablasPK;
import com.fenoreste.ws.rest.modelos.services.TablasService;
import com.fenoreste.ws.rest.Bankingly.dto.TablasDTO;

/**
 *
 * @author Fredy
 */
@Path("/products")
public class ProductsServices {
    
/*    
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
*/



    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response getProducts(String cadena) throws Throwable {
        JsonObjectBuilder ObjectBuilder = Json.createObjectBuilder();
        JsonArrayBuilder arrayEsqueleto = Json.createArrayBuilder();
        JsonObject ArrayProductos = null;
        JsonObject products = null;
        JsonObject JsonRegreso = null;
        JSONObject datosEntrada = new JSONObject(cadena);
        System.out.println("Accedio al metodo con:" + datosEntrada);

        ProductsDAO pr = new ProductsDAO();
        TablasService tablas = new TablasService();
        try {
            String ogs = datosEntrada.getString("ClientBankIdentifiers");
            Integer tipoProducto = datosEntrada.getInt("ProductTypes");
            int recorrido = 0;
            List<Object[]> ListaProductos = new ArrayList<Object[]>();
            List<Integer> ListaTipoProducto =  tablas.BuscarIdtablaDato1("bankingly", "lista_productos");
            System.out.println("va a entrar a tablas");
  System.out.println("ListaTipoProducto: " + ListaTipoProducto);
            TablasPK tpkcon = new TablasPK("bankingly", tipoProducto.toString());
            System.out.println("TablasPK: " + tpkcon);
            TablasDTO listaTab = tablas.buscaTabla(tpkcon);
            System.out.println("Tabla:  " + listaTab);

            System.out.println("tipoProducto: " + tipoProducto);
            // ListaTipoProducto.add(tipoProducto);
          //  if (!ListaTipoProducto.isEmpty()){
                System.out.println("TipoProducto");
                if (tipoProducto > 0) {
                    ListaTipoProducto.clear();
                    ListaTipoProducto.add(tipoProducto);
                } else {
                   System.out.println("tipoProducto > 0");
                   recorrido = ListaTipoProducto.size();
                }   
                // System.out.println("ListaTipoProducto: " + ListaTipoProducto);
                System.out.println("REcorrido: "+ recorrido);
                for (int i=0; i<= recorrido; i++){
                    System.out.println("i: " + i);
                    System.out.println("FREDY TIPOPRODUCTO: " + ListaTipoProducto.get(i));
                    // List<Object[]> ListaProductos = pr.getProducts(ogs, ListaTipoProducto.get(i));                      
                    ListaProductos = pr.getProducts(ogs, tipoProducto);                      
                    if (ListaProductos.size() > 0) {
                        System.out.println("Ya no entra");
                        for (Object[] prod : ListaProductos) {
                            System.out.println("entrea");
                            JsonObjectBuilder desc = Json.createObjectBuilder();
                            JsonObject DatosProd = desc
                                      .add("ClientBankIdentifier", String.valueOf(prod[0].toString()))
                                      .add("ProductBankIdentifier", String.valueOf(prod[1].toString()))
                                      .add("ProductNumber", String.valueOf(prod[2].toString()))
                                      .add("ProductStatusId", String.valueOf(prod[3].toString()))
                                      .add("ProductTypeId", tipoProducto.toString())
                                      .add("ProductAlias", String.valueOf(prod[4].toString()))
                                      .add("CanTransact", "0")
                                      .add("CurrencyId", "MXN")
                                      .build();
                            arrayEsqueleto.add(DatosProd);
                        }
                    } else {
                      //System.out.println("El socio:" + ogs + "no tiene productos con el tipo de producto: " + ListaTipoProducto.get(i));
                        System.out.println("El socio:" + ogs + "no tiene productos con el tipo de producto: ");
                    }
                   // products = ObjectBuilder.add("Product",arrayEsqueleto).build();
                    ArrayProductos=ObjectBuilder.add("Product",arrayEsqueleto).build();
                }
                JsonRegreso = ObjectBuilder.add("Products", ArrayProductos).build();
  /*          }else {
               System.out.println("Error no se encuentran la idTabla: bankingly  Parametro1: lista_productos");
            }
*/
            pr.cerrar();
            return Response.status(Response.Status.OK).entity(JsonRegreso.toString()).build(); 
        } catch (Exception e) {

            System.out.println("ERROR: " + e.getMessage());

            JsonRegreso = ObjectBuilder.add("type", "urn:vn:error-codes")
                    .add("title", "validation failed on submitted data").build();
            pr.cerrar();
            return Response.status(Response.Status.BAD_REQUEST).entity(JsonRegreso.toString()).build();
        } 
    }


/////////////////////////////////////////////////////////////////////////////////////////

 @POST
    @Path("/getProductsConsolidatedPosition")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response getProductsRate(String cadena) throws Throwable {
        JsonObjectBuilder ObjectBuilder = Json.createObjectBuilder();
        JsonArrayBuilder arrayEsqueleto = Json.createArrayBuilder();
        JsonObject ArrayProductos = null;
JsonObject JsonRegreso = null;
       // JsonObject DatosOK = null;
      //  JsonObject JsonNotFound = null;
        JsonObject JsonError = null;
        JSONObject datosEntrada = new JSONObject(cadena);
        ProductsDAO productosD = new ProductsDAO();
        try {
String ogs = datosEntrada.getString("ClientBankIdentifiers");
String opa = datosEntrada.getString("ProductBankIdentifiers");


  List<Object[]> ListaProductos = productosD.getProductsConsoli(ogs, opa);
 
                    if (ListaProductos.size() > 0) {
                        System.out.println("Ya no entra");
                        for (Object[] prod : ListaProductos) {
                            System.out.println("entrea");
                            JsonObjectBuilder descty = Json.createObjectBuilder();
                            JsonObject DatosProdconso = descty
                                      .add("ClientBankIdentifier", String.valueOf(prod[0].toString()))
                                      .add("ProductBankIdentifier", String.valueOf(prod[1].toString()))
                                      .add("ProductTypeId", String.valueOf(prod[2].toString()))

                                      .add("ProductAlias", String.valueOf(prod[3].toString()))
                                      .add("ProductNumber", String.valueOf(prod[4].toString()))
                                      .add("LocalCurrencyId", " ")
                                      .add("LocalBalance",  String.valueOf(prod[5].toString()))
                                      .add("InternationalCurrencyId", " ")
                                      .add("InternationalBalance", " ")
                                      .add("Rate", String.valueOf(prod[6].toString()) )
                                      .add("ExpirationDate", String.valueOf(prod[7].toString()) )
                                      .add("PaidFees",  " ")
                                      .add("Term", " ")
                                      .add("NextFeeDueDate", " ")
                                      .add("ProductOwnerName", String.valueOf(prod[15].toString()))
                                      .add("ProductBranchName", String.valueOf(prod[16].toString()))
                                      .add("CanTransact", "0")
                                      .add("SubsidiaryId", " " )
                                      .add("SubsidiaryName", " " )
                                      .add("BackendId", " " )
                                      .build();
                            arrayEsqueleto.add(DatosProdconso);
                        }
                    } else {
                      //System.out.println("El socio:" + ogs + "no tiene productos con el tipo de producto: " + ListaTipoProducto.get(i));
                        System.out.println("El socio:" + ogs + "no tiene productos con el tipo de producto: ");
                    }
                   // products = ObjectBuilder.add("Product",arrayEsqueleto).build();
                    ArrayProductos=ObjectBuilder.add("ProductConsolidatedPosition",arrayEsqueleto).build();

                JsonRegreso = ObjectBuilder.add("ProductConsolidatedPositionDataList", ArrayProductos).build();

return Response.status(Response.Status.OK).entity(JsonRegreso.toString()).build(); 


/*

            List<Object[]> ListaProductos = productosD.getProductsRate(customerId, productCode, acountType);
            if (ListaProductos.size() > 0) {
                for (Object[] prod : ListaProductos) {
                    JsonObjectBuilder desc = Json.createObjectBuilder();
                    DatosOK = desc
                            .add("warnings", ObjectBuilder.add("code", "string")
                                    .add("message", "string").build())
                            .add("interestRate", prod[0].toString())
                            .add("maturityDate", prod[1].toString())
                            .add("minInitialDepositAmount", ObjectBuilder.add("amount", prod[2].toString())
                                    .add("currecyCode", "code").build())
                            .add("property1", ObjectBuilder.add("description", prod[3].toString()).build())
                            .add("property2", ObjectBuilder.add("description", prod[3].toString()).build()).build();
                }
                return Response.status(Response.Status.OK).entity(DatosOK.toString()).build();
            } else {
                JsonNotFound = ObjectBuilder.add("type", "urn:vn:error-codes:" + customerId + "," + productCode + "," + acountType)
                        .add("tittle", "The requested object could not be found").build();
                return Response.status(Response.Status.NOT_FOUND).entity(JsonNotFound.toString()).build();
            }
*/
        } catch (Exception e) {
            JsonError = ObjectBuilder.add("type", "urn:vn:error-codes")
                    .add("title", "validation failed on submitted data").build();
            return Response.status(Response.Status.BAD_REQUEST).entity(JsonError.toString()).build();
        } finally {
            productosD.cerrar();
        }
    }

    
}
