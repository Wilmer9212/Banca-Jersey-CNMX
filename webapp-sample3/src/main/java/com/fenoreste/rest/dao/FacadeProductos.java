package com.fenoreste.rest.dao;

import com.fenoreste.rest.Util.AbstractFacade;
import com.fenoreste.rest.ResponseDTO.GetProductsConsolidatePositionDTO;
import com.fenoreste.rest.ResponseDTO.GetProductsDTO;
import com.fenoreste.rest.entidades.Amortizaciones;
import com.fenoreste.rest.entidades.AmortizacionesPK;
import com.fenoreste.rest.entidades.Auxiliares;
import com.fenoreste.rest.entidades.Catalog_Status_Bankingly;
import com.fenoreste.rest.entidades.Catalogo_Cuenta_Bankingly;
import com.fenoreste.rest.entidades.Productos;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

public abstract class FacadeProductos<T> {

    private static EntityManagerFactory emf;

    public FacadeProductos(Class<T> entityClass) {
        emf = AbstractFacade.conexion();
    }

    public List<GetProductsDTO> getProductos(String clientBankIdentifiers, Integer productTypes) {
        List<GetProductsDTO> ListagetP = new ArrayList<GetProductsDTO>();
        
        String productTypeId="",descripcion="";
        try {
            EntityManager em = emf.createEntityManager();
            String consulta = "";
            Catalogo_Cuenta_Bankingly ccb = null;
            if (!clientBankIdentifiers.equals("") && productTypes != null) {
                consulta = "SELECT * FROM auxiliares a WHERE replace((to_char(idorigen,'099999')||to_char(idgrupo,'09')||to_char(idsocio,'099999')),' ','')='" + clientBankIdentifiers + "' AND (SELECT producttypeid FROM tipos_cuenta_bankingly cb WHERE a.idproducto=cb.idproducto)=" + productTypes;
            } else if (!clientBankIdentifiers.equals("") && productTypes == null) {
                consulta = "SELECT * FROM auxiliares WHERE replace((to_char(idorigen,'099999')||to_char(idgrupo,'09')||to_char(idsocio,'099999')),' ','')='" + clientBankIdentifiers + "'";
            }

            Query query = em.createNativeQuery(consulta, Auxiliares.class);
            List<Auxiliares> ListaA = query.getResultList();
            
            
            for (int i = 0; i < ListaA.size(); i++) {
                GetProductsDTO auxi = new GetProductsDTO();
                Auxiliares a = ListaA.get(i);
                try {
                    ccb=em.find(Catalogo_Cuenta_Bankingly.class,a.getAuxiliaresPK().getIdproducto());
                    productTypeId=String.valueOf(ccb.getProductTypeId());
                    descripcion=ccb.getDescripcion();
                } catch (Exception e) {
                    productTypeId="";
                    descripcion="";
                }
                
                
                
                String og = String.format("%06d", a.getIdorigen()) + String.format("%02d", a.getIdgrupo());
                String s = String.format("%06d", a.getIdsocio());

                String op = String.format("%06d", a.getAuxiliaresPK().getIdorigenp()) + String.format("%05d", a.getAuxiliaresPK().getIdproducto());
                String aa = String.format("%08d", a.getAuxiliaresPK().getIdauxiliar());
                
                Catalog_Status_Bankingly ctb=em.find(Catalog_Status_Bankingly.class,Integer.parseInt(a.getEstatus().toString()));
                auxi = new GetProductsDTO(
                        og + s,
                        op + aa,
                        String.valueOf(a.getAuxiliaresPK().getIdproducto()),
                        ctb.getProductstatusid(),
                        productTypeId,
                        descripcion,
                        "1",
                        "1");
                ListagetP.add(auxi);
                productTypeId="";
                descripcion="";
            }
            System.out.println("Lista:" + ListagetP);

            return ListagetP;

        } catch (Exception e) {
            cerrar();
            e.getStackTrace();
            System.out.println("Error Producido:" + e.getMessage());
        }
        return null;
    }

    public List<GetProductsConsolidatePositionDTO> GetProductsConsolidatePosition(String clientBankIdentifier, String productBankIdentifier) {
        EntityManager em = emf.createEntityManager();
        String consulta = "SELECT * FROM auxiliares "
                + " WHERE replace((to_char(idorigen,'099999')||to_char(idgrupo,'09')||to_char(idsocio,'099999')),' ','')='" + clientBankIdentifier
                + "' AND replace((to_char(idorigenp,'099999')||to_char(idproducto,'09999')||to_char(idauxiliar,'09999999')),' ','')='" + productBankIdentifier + "'";
        List<GetProductsConsolidatePositionDTO> ListaReturn = new ArrayList<GetProductsConsolidatePositionDTO>();
        String productTypeId="";
        try {
            Query query = em.createNativeQuery(consulta, Auxiliares.class);
            List<Auxiliares> listaA = query.getResultList();
            boolean prA = false;
            for (int i = 0; i < listaA.size(); i++) {
                Auxiliares a = listaA.get(i);
                try {
                   Query queryR=em.createNativeQuery("SELECT producttypeid FROM tipos_cuenta_bankingly WHERE idproducto="+a.getAuxiliaresPK().getIdproducto());
                if(queryR!=null){
                productTypeId=String.valueOf(queryR.getSingleResult());                
                }
                 
                } catch (Exception e) {
                    
                    productTypeId="";
                }
                
               
                Productos pr = em.find(Productos.class, a.getAuxiliaresPK().getIdproducto());
                String Vence = "";
                try {
                    Query queryA = em.createNativeQuery("SELECT to_char(max(vence),'dd/MM/yyyy') FROM amortizaciones WHERE idorigenp=" + a.getAuxiliaresPK().getIdorigenp()
                            + " AND idproducto=" + a.getAuxiliaresPK().getIdproducto()
                            + " AND idauxiliar=" + a.getAuxiliaresPK().getIdauxiliar());
                    Vence = (String) queryA.getSingleResult();
                    System.out.println("VenceQ:" + Vence);
                    if (!Vence.equals("")) {
                        prA = true;
                    }
                } catch (Exception e) {
                    System.out.println("Error en amor:" + e.getMessage());
                    prA = false;
                }
                Date date = null;
                if (prA == true) {
                    date = stringTodate(Vence);
                }
                GetProductsConsolidatePositionDTO dto = new GetProductsConsolidatePositionDTO(
                        clientBankIdentifier,
                        productBankIdentifier,
                        productTypeId,
                        pr.getNombre(),
                        String.valueOf(a.getAuxiliaresPK().getIdproducto()),
                        1,
                        Double.parseDouble(a.getSaldo().toString()),
                        1,
                        Double.parseDouble(a.getSaldo().toString()),
                        Double.parseDouble(a.getSaldo().toString()),
                        date,
                        i,
                        Double.NaN,
                        a.getFechaAutorizacion(),
                        productBankIdentifier,
                        productBankIdentifier,
                        1,
                        i,
                        "",
                        i);

                ListaReturn.add(dto);
            }
            System.out.println("Lista:" + ListaReturn);
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
            cerrar();
        }

        return ListaReturn;

    }

    /*
    public List<Object[]> getProductsRate(String customerId, int productCode, String accountType) {
        EntityManager em = emf.createEntityManager();
        Query query = null;
        String consulta = "select (select tasaio from productos pr where pr.idproducto=a.idproducto),"
                + "(select plazomax from productos pr where pr.idproducto=a.idproducto)as pm,"
                + "20.00 as smin,"
                + "(select pr.nombre from productos pr where pr.idproducto=a.idproducto)as nombre"
                + " from auxiliares a where replace((to_char(a.idorigen,'099999')||to_char(idgrupo,'09')||to_char(idsocio,'099999')),' ','')='" + customerId + "' AND a.idproducto=" + productCode;//+" AND UPPER(pr.nombre) like '%"+accountType+"%'";
        List<Object[]> lista = null;
        try {
            query = em.createNativeQuery(consulta);
            lista = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.clear();
        }
        return query.getResultList();
    }
     */
    public List<Object[]> getProducts(String ogs, Integer tipoProducto) {
        String s = "";
        List<Object[]> lista = null;
        EntityManager em = emf.createEntityManager();
        Query query = null;
        System.out.println("ogs: " + ogs + "\n" + "tipoProducto: " + tipoProducto.toString());
        System.out.println("Accediendo a consulta...");
        String consulta = "SELECT TRIM(TO_CHAR(idorigen,'099999'))||TRIM(TO_CHAR(idgrupo,'09'))||TRIM(TO_CHAR(idsocio,'099999')) as ogs, "
                + "      TRIM(TO_CHAR(idorigenp,'099999'))||TRIM(TO_CHAR(idproducto,'09999'))||TRIM(TO_CHAR(idauxiliar,'09999999')) as opa, "
                + "      idproducto, "
                + "      (CASE WHEN estatus = 2 THEN 1 ELSE 0 END) as esatus, "
                + "      (SELECT nombre FROM productos pr WHERE pr.idproducto = a.idproducto) as nombre "
                + " FROM auxiliares a "
                + "WHERE estatus = 2 "
                + "  AND TRIM(TO_CHAR(idorigen,'099999'))||TRIM(TO_CHAR(idgrupo,'09'))||TRIM(TO_CHAR(idsocio,'099999')) = TRIM('" + ogs + "') "
                + "  AND idproducto in (SELECT idproduc\\:\\:INTEGER "
                + "                       FROM regexp_split_to_table((SELECT dato2 "
                + "                                                     FROM tablas "
                + "                                                    WHERE idtabla = 'bankingly' "
                + "                                                      AND idelemento = '" + tipoProducto + "'), ',') AS idproduc);";

        System.out.println("Consuuuuuuuuuuuuuuuuuuuulta: \n" + consulta);
        /*
        try {
            // s = new String(consulta.getBytes(),"ISO-8859-1");
            s =  new String(consulta.getBytes("ISO-8859-1"), "UTF-8");
            System.out.println("si:" + s);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(FacadeProductos.class.getName()).log(Level.SEVERE, null, ex);
        }
         */
        try {
            // query = em.createNativeQuery(s);
            query = em.createNativeQuery(consulta);
            lista = query.getResultList();
            System.out.println("Resultados obtenidos: " + lista);
        } catch (Exception e) {
            em.clear();
            em.close();
            System.out.println("No pudo obtener datos \n Error: " + e.getMessage());
        } finally {
            em.clear();
            em.close();
        }
        // return query.getResultList();
        return lista;
    }

    public List<Object[]> getProductsConsoli(String ogs, String opa) {
        String s = "";
        List<Object[]> lista = null;
        EntityManager em = emf.createEntityManager();
        Query query = null;
        System.out.println("ogs: " + ogs + "\n" + "opa: " + opa);

        String consulta = "SELECT TRIM(TO_CHAR(a.idorigen,'099999'))||TRIM(TO_CHAR(a.idgrupo,'09'))||TRIM(TO_CHAR(a.idsocio,'099999')) as ogs, "
                + "      TRIM(TO_CHAR(a.idorigenp,'099999'))||TRIM(TO_CHAR(a.idproducto,'09999'))||TRIM(TO_CHAR(a.idauxiliar,'09999999')) as opa, "
                + "      (SELECT idelemento FROM (SELECT * FROM (SELECT regexp_split_to_table(dato2,',') AS product, "
                + "                                                     idelemento "
                + "                                                FROM tablas "
                + "                                               WHERE idtabla = 'bankingly' "
                + "                                                 AND dato1 = 'lista_productos') AS pro "
                + "                                WHERE TRIM(product) is not null AND TRIM(product) <> '') AS pr "
                + "        WHERE product::INTEGER = (select dato from prueba)) AS ProductTypeId, "
                + "      pr.nombre, "
                + "      a.idproducto, "
                + "      a.saldo, "
                + "      (case when tipoproducto in (0,1,8) and tasaiod is null and tasaim is null then tasaio else 0 end) as tasa, "
                + "      (case when tipoproducto in (1,8) then date(fechaactivacion + (plazo||' month'||\\:\\:interval)) else '01/01/1999' end) as fecha_vence, "
                + "      a.plazo, "
                + "      a.fechaactivacion, "
                + "      pr.tipoproducto, "
                + "      (SELECT count(*) FROM amortizaciones am WHERE am.idorigenp = a.idorigenp AND am.idproducto = a.idproducto AND am.idauxiliar = a.idauxiliar AND am.todopag = TRUE) AS pagos_realizados, "
                + "      (SELECT count(*) FROM amortizaciones am WHERE am.idorigenp = a.idorigenp AND am.idproducto = a.idproducto AND am.idauxiliar = a.idauxiliar) AS totales_mensualidades, "
                + "      (SELECT count(*) FROM amortizaciones am WHERE am.idorigenp = a.idorigenp AND am.idproducto = a.idproducto AND am.idauxiliar = a.idauxiliar AND am.) AS pox_pag, "
                + "      (SELECT o.nombre FROM origenes o WHERE o.idorigen = a.idorigenp) AS sucursal, "
                + "      (SELECT p.nombre||' '||p.appaterno||' '||p.apmaterno FROM personas p WHERE p.idorigen = a.idorigen AND p.idgrupo = a.idgrupo AND p.idsocio = a.idsocio) as socio,  "
                + "      (SELECT su.nombre from origenes su where su.idorigen = a.idorigenp) as sucursal "
                + " FROM auxiliares a "
                + " INNER JOIN productos pr USING(idproducto) "
                + "WHERE a.estatus = 2 "
                + "  AND TRIM(TO_CHAR(a.idorigen,'099999'))||TRIM(TO_CHAR(a.idgrupo,'09'))||TRIM(TO_CHAR(a.idsocio,'099999')) = TRIM('" + ogs + "') "
                + "  AND TRIM(TO_CHAR(a.idorigenp,'099999'))||TRIM(TO_CHAR(a.idproducto,'09999'))||TRIM(TO_CHAR(a.idauxiliar,'09999999')) = TRIM('" + opa + "'); ";

        System.out.println("Consuuuuuuuuuuuuuuuuuuuulta: \n" + consulta);

        try {
            // query = em.createNativeQuery(s);
            query = em.createNativeQuery(consulta);
            lista = query.getResultList();
            System.out.println("Resultados obtenidos: " + lista);
        } catch (Exception e) {
            em.clear();
            em.close();
            System.out.println("No pudo obtener datos \n Error: " + e.getMessage());
        } finally {
            em.clear();
            em.close();
        }
        // return query.getResultList();
        return lista;
    }

    public static Date stringTodate(String fecha) {
        Date date = null;
        try {
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            date = formato.parse(fecha);
        } catch (ParseException ex) {
            System.out.println("Error al convertir fecha:" + ex.getMessage());
        }
        System.out.println("date:" + date);
        return date;
    }

    public void cerrar() {
        emf.close();
    }

}
