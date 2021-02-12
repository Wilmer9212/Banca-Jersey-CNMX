package com.fenoreste.ws.rest.dao;

import com.fenoreste.ws.rest.Bankingly.dto.GetProductsDTO;
import com.fenoreste.ws.rest.Util.*;
import com.fenoreste.ws.rest.modelos.entidad.Auxiliares;
import com.fenoreste.ws.rest.modelos.entidad.Catalogo_Cuenta_Bankingly;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Formatter;
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

    public List<GetProductsDTO> getProductos(String productBI,String productTp) {        
        
        List<GetProductsDTO>ListagetP=new ArrayList<GetProductsDTO>();
        GetProductsDTO auxi=new GetProductsDTO();
        try{
        EntityManager em=emf.createEntityManager();
        
        String consulta="";
        if(!productBI.equals("") && !productTp.equals("")){
            consulta="SELECT * FROM auxiliares WHERE replace((to_char(idorigenp,'099999')||to_char(idproducto,'09999')||to_char(idauxiliar,'09999999')),' ','')='"+productBI+"' AND idproducto="+productTp;
        }else if(!productBI.equals("") && productTp.equals("")){
            consulta="SELECT * FROM auxiliares WHERE replace((to_char(idorigenp,'099999')||to_char(idproducto,'09999')||to_char(idauxiliar,'09999999')),' ','')='"+productBI+"'";
        }else if(productBI.equals(em) && !productTp.equals("")){
            consulta="SELECT * FROM auxiliares WHERE idproducto="+Integer.parseInt(productTp);
        }else{
            consulta="SELECT * FROM auxiliares WHERE estatus=2";
        }
        
        
        Query query=em.createNativeQuery(consulta,Auxiliares.class);
        List<Auxiliares>ListaA=query.getResultList();
        
        for(int i=0;i<ListaA.size();i++){
            Auxiliares a=ListaA.get(i);
            Catalogo_Cuenta_Bankingly catalogoCuentas=em.find(Catalogo_Cuenta_Bankingly.class,a.getAuxiliaresPK().getIdproducto());
            
            String og=String.format("%06d",a.getIdorigen())+String.format("%02d",a.getIdgrupo());
            String s=String.format("%06d",a.getIdsocio());
            
            String op=String.format("%06d",a.getAuxiliaresPK().getIdorigenp())+String.format("%05d",a.getAuxiliaresPK().getIdproducto());
            String aa=String.format("%08d",a.getAuxiliaresPK().getIdauxiliar());
            
            
            auxi= new GetProductsDTO(og+s,
                                    op+aa,//"%06d",a.getAuxiliaresPK().getIdorigenp())+""+p.format("%05d",a.getAuxiliaresPK().getIdproducto())+""+a1.format("%08d",a.getAuxiliaresPK().getIdauxiliar()),
                                    String.valueOf(a.getAuxiliaresPK().getIdproducto()),
                                    "",
                                    productTp,  
                                    productBI,
                                    consulta,
                                    productTp);
            ListagetP.add(auxi);
          
        }
        System.out.println("Lista:"+ListagetP);
        
        
        return ListagetP;
        }catch(Exception e){
            System.out.println("Error Producido:"+e.getMessage());
        }
        
        return null;
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



    public void cerrar(){
        emf.close();
    }

}
