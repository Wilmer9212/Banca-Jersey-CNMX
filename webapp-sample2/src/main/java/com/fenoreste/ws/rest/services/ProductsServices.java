
package com.fenoreste.ws.rest.services;


import com.fenoreste.ws.rest.Bankingly.dto.GetProductsConsolidatePositionDTO;
import com.fenoreste.ws.rest.Bankingly.dto.GetProductsDTO;
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
import org.codehaus.jettison.json.JSONObject;
import com.fenoreste.ws.rest.dao.ProductsDAO;
import java.math.BigDecimal;
import org.codehaus.jettison.json.JSONArray;
/**
 *
 * @author Wilmer
 */
@Path("/Products")
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
                    jsonD = json.add("clientBankIdentifier", dto.getClientBankIdentifier().toString())
                            .add("productBankIdentifier", dto.getProductBankIdentifier().toString())
                            .add("productNumber", dto.getProductNumber().toString())
                            .add("productStatusId", String.valueOf(dto.getProductStatusId().toString()))
                            .add("productTypeId", dto.getProductTypeId().toString())
                            .add("productAlias", dto.getProductAlias().toString())
                            .add("canTransact", dto.getCanTransact().toString())                            
                            .add("currencyId",dto.getCurrencyId().toString())
                            .build();
                    jsona.add(jsonD);
                    
                JsonObject jsonRegreso = (JsonObject) Json.createObjectBuilder().add("Products", jsona).build();
                return Response.status(Response.Status.OK).entity(jsonRegreso).build();
                }
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
                JsonObject k = Json.createObjectBuilder().add("ConsolidatedPosition", jsona).build();
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
}
