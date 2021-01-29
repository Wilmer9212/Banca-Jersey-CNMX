package com.fenoreste.ws.rest.dao;

import com.fenoreste.ws.rest.Util.*;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

public abstract class FacadeProductos<T> {

    private static EntityManagerFactory emf;
    private static final String PERSISTENCE_UNIT_NAME = "conexion";

    public FacadeProductos(Class<T> entityClass) {
        emf = AbstractFacade.conexion();//Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);    
    }

    public List<Object[]> getProducts1() {
        String s = "";
        EntityManager em = emf.createEntityManager();
        Query query = null;
        System.out.println("Accediendo a consulta...");
        String consulta = "select idproducto,(case when tipoproducto=2 then 'Prestamo' else 'Ahorro' end) as pr,nombre from productos";
        try {
            // s = new String(consulta.getBytes(),"ISO-8859-1");
            s = new String(consulta.getBytes("ISO-8859-1"), "UTF-8");
            System.out.println("si:" + s);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(FacadeProductos.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<Object[]> lista = null;
        try {
            query = em.createNativeQuery(s);
            lista = query.getResultList();
            System.out.println("Resultados obtenidos:" + lista.size());
        } catch (Exception e) {
            System.out.println("No pudo obtener datos");
        } finally {
            em.clear();
        }
        return query.getResultList();
    }

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

    public void cerrar() throws Throwable {
        emf.close();
    }
}
