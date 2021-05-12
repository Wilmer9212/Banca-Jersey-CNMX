package com.fenoreste.rest.dao;

import com.fenoreste.rest.Util.AbstractFacade;
import com.fenoreste.rest.ResponseDTO.ProductsConsolidatePositionDTO;
import com.fenoreste.rest.ResponseDTO.ProductsDTO;
import com.fenoreste.rest.entidades.Auxiliares;
import com.fenoreste.rest.entidades.Catalog_Status_Bankingly;
import com.fenoreste.rest.entidades.Catalogo_Cuenta_Bankingly;
import com.fenoreste.rest.entidades.Persona;
import com.fenoreste.rest.entidades.PersonasPK;
import com.fenoreste.rest.entidades.Productos;
import com.fenoreste.rest.entidades.Tablas;
import com.fenoreste.rest.entidades.TablasPK;
import com.fenoreste.rest.entidades.WsFoliosTarjetasSyC1;
import com.fenoreste.rest.entidades.WsFoliosTarjetasSyCPK1;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

public abstract class FacadeProductos<T> {

    private static EntityManagerFactory emf;

    public FacadeProductos(Class<T> entityClass) {
        emf = AbstractFacade.conexion();
    }

    public List<ProductsDTO> getProductos(String clientBankIdentifiers, Integer productTypes) {
        List<ProductsDTO> ListagetP = new ArrayList<ProductsDTO>();

        String productTypeId = "", descripcion = "";
        try {
            EntityManager em = emf.createEntityManager();
            String consulta = "";
            Catalogo_Cuenta_Bankingly ccb = null;
            if (!clientBankIdentifiers.equals("") && productTypes != null) {
                consulta = "SELECT * FROM auxiliares a INNER JOIN tipos_cuenta_bankingly pr USING(idproducto) WHERE replace((to_char(a.idorigen,'099999')||to_char(a.idgrupo,'09')||to_char(a.idsocio,'099999')),' ','')='" + clientBankIdentifiers + "' AND (SELECT producttypeid FROM tipos_cuenta_bankingly cb WHERE a.idproducto=cb.idproducto)=" + productTypes+" AND a.estatus=2";
            } else if (!clientBankIdentifiers.equals("") && productTypes == null) {
                consulta = "SELECT * FROM auxiliares a INNER JOIN tipos_cuenta_bankingly USING(idproducto) WHERE replace((to_char(a.idorigen,'099999')||to_char(a.idgrupo,'09')||to_char(a.idsocio,'099999')),' ','')='" + clientBankIdentifiers + "' AND a.estatus=2";
            }
            System.out.println("Consulta:" + consulta);
            Query query = em.createNativeQuery(consulta, Auxiliares.class);
            List<Auxiliares> ListaA = query.getResultList();
            
            //Identifico la caja para la TDD
            Tablas tb=null;
            try{           
            TablasPK pkt=new TablasPK("identificador_uso_tdd","activa");
            tb=em.find(Tablas.class, pkt);
            }catch(Exception e){
                System.out.println("No se encontro la tabla:"+e.getMessage());
            }
            for (int i = 0; i < ListaA.size(); i++) {
                ProductsDTO auxi = new ProductsDTO();
                Auxiliares a = ListaA.get(i);
                
                if(tb.getDato1().equals("1")){
                    DAOTDD ws=new DAOTDD();
                    if(a.getAuxiliaresPK().getIdproducto()==Integer.parseInt(tb.getDato2())){
                        System.out.println("idorigenp:"+a.getAuxiliaresPK().getIdorigenp()+",idproducto:"+a.getAuxiliaresPK().getIdproducto()+",idauxiliar:"+a.getAuxiliaresPK().getIdauxiliar());
                        WsFoliosTarjetasSyCPK1 pk1=new WsFoliosTarjetasSyCPK1(a.getAuxiliaresPK().getIdorigenp(),a.getAuxiliaresPK().getIdproducto(),a.getAuxiliaresPK().getIdauxiliar());
                        WsFoliosTarjetasSyC1 folios=em.find(WsFoliosTarjetasSyC1.class,pk1);
                        System.out.println("Folio:"+folios);
                        if(pk1!=null){
                            if(folios.getActiva()){
                                System.out.println("si");
                            }
                        }
                        System.out.println("pkg1:"+pk1);
                        WsFoliosTarjetasSyC1 sc1=em.find(WsFoliosTarjetasSyC1.class,pk1);
                        System.out.println("sc1:"+sc1);                        
                    }
                }
                try {
                    ccb = em.find(Catalogo_Cuenta_Bankingly.class, a.getAuxiliaresPK().getIdproducto());
                    productTypeId = String.valueOf(ccb.getProductTypeId());
                    descripcion = ccb.getDescripcion();
                } catch (Exception e) {
                    productTypeId = "";
                    descripcion = "";
                }

                String og = String.format("%06d", a.getIdorigen()) + String.format("%02d", a.getIdgrupo());
                String s = String.format("%06d", a.getIdsocio());

                String op = String.format("%06d", a.getAuxiliaresPK().getIdorigenp()) + String.format("%05d", a.getAuxiliaresPK().getIdproducto());
                String aa = String.format("%08d", a.getAuxiliaresPK().getIdauxiliar());

                Catalog_Status_Bankingly ctb = new Catalog_Status_Bankingly();
                int sttt = 0;
                try {
                    ctb = em.find(Catalog_Status_Bankingly.class, Integer.parseInt(a.getEstatus().toString()));
                    sttt = ctb.getProductstatusid();
                } catch (Exception ex) {
                    sttt = 0;
                }
                auxi = new ProductsDTO(
                        og + s,
                        op + aa,
                        String.valueOf(a.getAuxiliaresPK().getIdproducto()),
                        sttt,
                        productTypeId,
                        descripcion,
                        "1",
                        "1");
                ListagetP.add(auxi);
                productTypeId = "";
                descripcion = "";
            }
            System.out.println("Lista:" + ListagetP);

            return ListagetP;

        } catch (Exception e) {
            e.getStackTrace();
            System.out.println("Error Producido:" + e.getMessage());
        }
        return null;
    }

    public List<ProductsConsolidatePositionDTO> ProductsConsolidatePosition(String clientBankIdentifier, List<String> productsBank) {
        EntityManager em = emf.createEntityManager();
        List<ProductsConsolidatePositionDTO> ListaReturn = new ArrayList<ProductsConsolidatePositionDTO>();
        String productTypeId = "";
        for (int ii = 0; ii < productsBank.size(); ii++) {
            String consulta = "SELECT * FROM auxiliares "
                    + " WHERE replace((to_char(idorigen,'099999')||to_char(idgrupo,'09')||to_char(idsocio,'099999')),' ','')='" + clientBankIdentifier
                    + "' AND replace((to_char(idorigenp,'099999')||to_char(idproducto,'09999')||to_char(idauxiliar,'09999999')),' ','')='" + productsBank.get(ii) + "' AND estatus=2";
            try {
                Query query = em.createNativeQuery(consulta, Auxiliares.class);
                List<Auxiliares> listaA = query.getResultList();
                boolean prA = false;
                for (int i = 0; i < listaA.size(); i++) {
                    Auxiliares a = listaA.get(i);
                    try {
                        Query queryR = em.createNativeQuery("SELECT producttypeid FROM tipos_cuenta_bankingly WHERE idproducto=" + a.getAuxiliaresPK().getIdproducto());
                        if (queryR != null) {
                            productTypeId = String.valueOf(queryR.getSingleResult());
                        }

                    } catch (Exception e) {

                        productTypeId = "";
                    }

                    Productos pr = em.find(Productos.class, a.getAuxiliaresPK().getIdproducto());
                    String sai = "";
                    String vencimiento = "";
                    Double tasa = 0.0;
                    Catalogo_Cuenta_Bankingly ctb = em.find(Catalogo_Cuenta_Bankingly.class, a.getAuxiliaresPK().getIdproducto());
                    if (ctb.getProductTypeId() == 4) {
                        try {
                            Query queryA = em.createNativeQuery("SELECT * FROM sai_auxiliar(" + a.getAuxiliaresPK().getIdorigenp() + ","
                                    + a.getAuxiliaresPK().getIdproducto() + "," + a.getAuxiliaresPK().getIdauxiliar() + ",(SELECT date(fechatrabajo) FROM origenes LIMIT 1))");
                            System.out.println("Consulta:" + queryA.getSingleResult());
                            sai = (String) queryA.getSingleResult();
                            String[] parts = sai.split("\\|");
                            List list = Arrays.asList(parts);
                            vencimiento = (String) list.get(2);
                            tasa = Double.parseDouble(a.getTasaio().toString());
                            System.out.println("Vencimiento:" + vencimiento);
                        } catch (Exception e) {
                            System.out.println("Error en amor:" + e.getMessage());
                            prA = false;
                        }
                    }

                    int cpagadas = 0;
                    Double totalam = 0.0;
                    String vencep="";
                    if (ctb.getProductTypeId() == 5) {
                        String consulta1 = "SELECT count(*) FROM amortizaciones am WHERE idorigenp=" + a.getAuxiliaresPK().getIdorigenp()
                                + " AND idproducto=" + a.getAuxiliaresPK().getIdproducto()
                                + " AND idauxiliar=" + a.getAuxiliaresPK().getIdauxiliar()
                                + " AND todopag=true";
                        
                        System.out.println("Consulta Prestamo:" + consulta1);
                        Query query1 = em.createNativeQuery(consulta1);
                        cpagadas = Integer.parseInt(query1.getSingleResult().toString());

                        String consulta2 = "SELECT count(*) FROM amortizaciones am WHERE idorigenp=" + a.getAuxiliaresPK().getIdorigenp()
                                + " AND idproducto=" + a.getAuxiliaresPK().getIdproducto()
                                + " AND idauxiliar=" + a.getAuxiliaresPK().getIdauxiliar();
                        System.out.println("Consulta Prestamo:" + consulta2);
                        Query query2 = em.createNativeQuery(consulta2);
                        totalam = Double.parseDouble(query2.getSingleResult().toString());
                        System.out.println("totalAm:" + totalam);
                        
                        Query queryA = em.createNativeQuery("SELECT * FROM sai_auxiliar(" + a.getAuxiliaresPK().getIdorigenp() + ","
                                    + a.getAuxiliaresPK().getIdproducto() + "," + a.getAuxiliaresPK().getIdauxiliar() + ",(SELECT date(fechatrabajo) FROM origenes LIMIT 1))");
                        
                        sai = (String) queryA.getSingleResult();
                            String[] parts = sai.split("\\|");
                        List list = Arrays.asList(parts);
                        
                       
                        vencep=(String) list.get(10);
                        System.out.println("Vence:"+vencep);
                        }
                        
                        PersonasPK pk=new PersonasPK(a.getIdorigen(),a.getIdgrupo(),a.getIdsocio());
                        Persona p=em.find(Persona.class,pk);
                        
                        String nmsucursal="";
                        try {
                        Query queryO=em.createNativeQuery(
                                                          "SELECT nombre FROM origenes WHERE idorigen="+a.getAuxiliaresPK().getIdorigenp());
                        nmsucursal=(String) queryO.getSingleResult();
                        } catch (Exception e) {
                            System.out.println("Error en buscar nombre de la sucursal:"+e.getMessage());
                        }
                    ProductsConsolidatePositionDTO dto = new ProductsConsolidatePositionDTO(
                            clientBankIdentifier,
                            productsBank.get(ii),
                            productTypeId,
                            pr.getNombre(),
                            String.valueOf(a.getAuxiliaresPK().getIdproducto()),
                            1,
                            Double.parseDouble(a.getSaldo().toString()),
                            1,
                            Double.parseDouble(a.getSaldo().toString()),
                            tasa,
                            vencimiento,
                            cpagadas,
                            totalam,
                            vencep,
                            p.getNombre()+" "+p.getAppaterno()+" "+p.getApmaterno(),
                            nmsucursal.toUpperCase(),
                            1,
                            1,
                            "",
                            1);

                    ListaReturn.add(dto);
                }
                System.out.println("Lista:" + ListaReturn);
            } catch (Exception e) {
                System.out.println("Error:" + e.getMessage());
                cerrar();
            }
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
