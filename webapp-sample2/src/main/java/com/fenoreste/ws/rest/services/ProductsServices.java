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
import com.fenoreste.ws.rest.Bankingly.dto.GetProductsConsolidatePositionDTO;
import com.fenoreste.ws.rest.Bankingly.dto.GetProductsDTO;
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
import java.math.BigDecimal;
import java.util.Date;
import org.codehaus.jettison.json.JSONArray;

/**
 *
 * @author Fredy
 */
@Path("/products")
public class ProductsServices {

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response GetPRoducts(String cadena) throws Throwable {
        JsonObjectBuilder json = Json.createObjectBuilder();
        JsonArrayBuilder jsona = Json.createArrayBuilder();
        String ClientBankIdentifiers = "";
        Integer ProductTypes = null;
        try {
            JSONObject Object = new JSONObject(cadena);
            JSONArray jsonCB = Object.getJSONArray("ClientBankIdentifiers");
            JSONArray jsonPB = Object.getJSONArray("ProductTypes");
            for (int i = 0; i < jsonCB.length(); i++) {
                JSONObject jCB = (JSONObject) jsonCB.get(i);
                ClientBankIdentifiers = jCB.getString("value");
                System.out.println("ClientBankIdentifiers:" + ClientBankIdentifiers);
            }
            for (int x = 0; x < jsonPB.length(); x++) {
                JSONObject jPB = (JSONObject) jsonPB.get(x);
                ProductTypes = jPB.getInt("value");
                System.out.println("ProductTypes:" + ProductTypes);
            }
        } catch (Exception e) {
            System.out.println("Error al convertir Json:" + e.getMessage());
        }
        ProductsDAO dao = new ProductsDAO();
        try {
            List<GetProductsDTO> listaDTO = dao.getProductos(ClientBankIdentifiers, ProductTypes);
            if (listaDTO != null) {
                JsonObject jsonD = null;
                for (int i = 0; i < listaDTO.size(); i++) {
                    GetProductsDTO dto = listaDTO.get(i);
                    jsonD = json.add("clientBankIdentifier", dto.getClientBankIdentifier())
                            .add("productBankIdentifier", dto.getProductBankIdentifier())
                            .add("productNumber", dto.getProductNumber())
                            .add("productStatusId", String.valueOf(dto.getProductStatusId()))
                            .add("productTypeId", dto.getProductTypeId())
                            .add("productAlias", dto.getProductAlias())
                            .add("canTransact", dto.getCanTransact())
                            .build();
                    jsona.add(jsonD);
                }
                JsonObject jsonRegreso = (JsonObject) Json.createObjectBuilder().add("GetProducts", jsona).build();
                return Response.status(Response.Status.OK).entity(jsonRegreso).build();
            } else {
                JsonObject jsonk = Json.createObjectBuilder().add("Error", "Datos no encontrados").build();
                return Response.status(Response.Status.NO_CONTENT).entity(jsonk).build();
            }

        } catch (Exception e) {
            System.out.println("Error interno en el servidor");
            dao.cerrar();
        } finally {
            dao.cerrar();
        }

        return null;

    }

    @POST
    @Path("/ConsolidatedPosition")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response getProductsConsolidatedPosition(String cadena) {
        /*SOLO FALTA DEL CATALOGO CAN TRANSACT ID*/
        String ClientBankIdentifiers = "", ProductBankIdentifiers = "";
        try {
            JSONObject Object = new JSONObject(cadena);
            JSONArray jsonCB = Object.getJSONArray("ClientBankIdentifiers");
            JSONArray jsonPB = Object.getJSONArray("ProductBankIdentifiers");
            for (int i = 0; i < jsonCB.length(); i++) {
                JSONObject jCB = (JSONObject) jsonCB.get(i);
                ClientBankIdentifiers = jCB.getString("value");
                System.out.println("ClientBankIdentifiers:" + ClientBankIdentifiers);
            }
            for (int x = 0; x < jsonPB.length(); x++) {
                JSONObject jPB = (JSONObject) jsonPB.get(x);
                ProductBankIdentifiers = jPB.getString("value");
                System.out.println("ProductBankIdentifiers:" + ProductBankIdentifiers);
            }
        } catch (Exception e) {
            System.out.println("Error al convertir Json:" + e.getMessage());
        }

        ProductsDAO dao = new ProductsDAO();
        try {
            List<GetProductsConsolidatePositionDTO> listaJson = dao.GetProductsConsolidatePosition(ClientBankIdentifiers, ProductBankIdentifiers);
            JsonArrayBuilder jsona = Json.createArrayBuilder();
            JsonObject jsonD = null;
            System.out.println("ListaJson:Size:" + listaJson.size());
            if (listaJson != null) {
                for (int i = 0; i < listaJson.size(); i++) {
                    System.out.println("Entro");
                    GetProductsConsolidatePositionDTO dto = listaJson.get(i);
                    System.out.println("DTO:" + dto);
                    JsonObjectBuilder json = Json.createObjectBuilder();
                    jsonD = json.add("clientBankIdentifier", dto.getClientBankIdentifier())
                            .add("productBankIdentifier", dto.getProductBranchName())
                            .add("productTypeId", String.valueOf(dto.getProductTypeId()))
                            .add("productAlias", dto.getProductAlias())
                            .add("productNumber", String.valueOf(dto.getProductNumber()))
                            .add("localCurrencyId", dto.getLocalCurrencyId())
                            .add("localBalance", String.valueOf(dto.getLocalBalance()))
                            .add("internationalCurrencyId", dto.getInternationalCurrencyId())
                            .add("internationalBalance", String.valueOf(dto.getInternationalBalance()))
                            .add("rate", String.valueOf(dto.getRate()))
                            .add("expirationDate", String.valueOf(dto.getExpirationDate()))
                            .add("paidFees", String.valueOf(dto.getPaidFees()))
                            .add("term", String.valueOf(dto.getTerm()))
                            .add("nextFeeDueDate", String.valueOf(dto.getNextFeeDueDate()))
                            .add("productOwnerName", dto.getProductOwnerName())
                            .add("productBranchName", dto.getProductBranchName())
                            .add("canTransact", String.valueOf(dto.getCanTransact()))
                            .add("subsidiaryId", String.valueOf(dto.getSubsidiaryId()))
                            .add("subsidiaryName", dto.getSubsidiaryName())
                            .add("backendId", String.valueOf(dto.getBackendId()))
                            .build();

                    jsona.add(jsonD);
                    System.out.println("jsona:" + jsona);
                JsonObject k = Json.createObjectBuilder().add("Products", jsona).build();
                return Response.status(Response.Status.OK).entity(k).build();
                }
            } else {
                JsonObject jsonk = Json.createObjectBuilder().add("Error", "Datos no encontrados").build();
                return Response.status(Response.Status.NO_CONTENT).entity(jsonk).build();
            }
        } catch (Exception e) {
            System.out.println("Error aqui:" + e.getMessage());
            dao.cerrar();
        } finally {
            dao.cerrar();
        }

        return null;

    }
    /*  
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
            List<Integer> ListaTipoProducto = tablas.BuscarIdtablaDato1("bankingly", "lista_productos");
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
            System.out.println("REcorrido: " + recorrido);
            for (int i = 0; i <= recorrido; i++) {
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
                ArrayProductos = ObjectBuilder.add("Product", arrayEsqueleto).build();
            }
            JsonRegreso = ObjectBuilder.add("Products", ArrayProductos).build();
            /*          }else {
               System.out.println("Error no se encuentran la idTabla: bankingly  Parametro1: lista_productos");
            }
             
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
     */

}
