/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fenoreste.ws.rest.modelos.services;

import com.fenoreste.ws.rest.Bankingly.dto.TablasDTO;
import com.fenoreste.ws.rest.modelos.entidad.TablasPK;
import com.fenoreste.ws.rest.modelos.entidad.Tablas;
import com.fenoreste.ws.rest.Bankingly.dto.TablasDTO;

import com.fenoreste.ws.rest.Util.AbstractFacade;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author Fredy
 */
public class TablasService {


private static EntityManagerFactory emf;
    private static final String PERSISTENCE_UNIT_NAME = "conexion";

    public TablasService() {
        emf = AbstractFacade.conexion();//Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);    
    }

    public TablasDTO buscaTabla(TablasPK tablaspk) {
        // entity = tablasFacade.getEntityManager();
        EntityManager em = emf.createEntityManager();
        TablasDTO tablasDTO = new TablasDTO();
        String nombre = "";
        String Dato1 = "";
        String Dato2 = "";
        String Dato3 = "";
        String Dato4 = "";
        String Dato5 = "";
        short Tipo = 0;

        // TablasPK tpkcon = new TablasPK();
        List<Object[]> lista = null;
        try {
            String consulta = " SELECT t.* "
                            + "         FROM tablas t "
                            + "         WHERE t.idtabla = '" + tablaspk.getIdtabla() + "'" 
                            + "           AND t.idelemento = '" + tablaspk.getIdelemento() + "';";
            // System.out.println("Consulta SQL: " + consulta);
            Query query = em.createNativeQuery(consulta);
            lista = query.getResultList();

            if (lista.size() > 0){           
                for (Object[] tab : lista) {

                    if (tab[2] != null) {nombre = tab[2].toString();} 
                    if (tab[3] != null) {Dato1 = tab[3].toString();} 
                    if (tab[4] != null) {Dato2 = tab[4].toString();} 
                    if (tab[5] != null) {Dato3 = tab[5].toString();} 
                    if (tab[6] != null) {Dato4 = tab[6].toString();} 
                    if (tab[7] != null) {Dato5 = tab[7].toString();} 
                    if (tab[8] != null) {Tipo = Short.parseShort(tab[8].toString());} 

                    tablasDTO.setNombre(nombre);
                    tablasDTO.setDato1(Dato1);
                    tablasDTO.setDato2(Dato2);
                    tablasDTO.setDato3(Dato3);
                    tablasDTO.setDato4(Dato4);
                    tablasDTO.setDato5(Dato5);
                    tablasDTO.setTablasPK(tablaspk);
                    tablasDTO.setTipo(Tipo);
                }
            }
        } catch (Exception e) {
                em.clear();
                em.close();
                System.out.println("Error en buscaTabla de TablasService: " + e.getMessage());
        }
        em.clear();
        em.close();
        return tablasDTO;
    }

    public List<Integer> BuscarIdtablaDato1(String idtabla, String dato1) {
         List<Object[]> lista = null;
         EntityManager em = emf.createEntityManager();
         List<Integer> listaProducto = new ArrayList<Integer>();
         Integer tipoProduct = null;
         
         try {
             String consulta = "SELECT * FROM tablas WHERE idtabla = '" + idtabla + "' AND dato1 = '" + dato1 + "' ORDER BY idelemento ASC;" ;
             Query query = em.createNativeQuery(consulta);
             lista = query.getResultList();
             System.out.println("Tamano de lista: " + lista.size());
             if (lista.size() > 0){
               // total = Integer.parseInt(resultado);
               System.out.println("Lsita de objeto: " + lista);
               for (Object[] prod : lista){
                System.out.println("Prod: " + prod[1]);
                   System.out.println("lista recorido: " + prod[1].toString());
                   tipoProduct = Integer.parseInt(prod[1].toString().trim());
                   System.out.println("TipoProducto: " + tipoProduct);
                   if (tipoProduct >= 0){
                      //listaProducto.add(Integer.parseInt(tipoProduct));
                      listaProducto.add(tipoProduct);
                      System.out.println("ListaProducto: " + listaProducto);
                   }
               }
             }else {
               System.out.println("No existen Registros BuscarIdtablaDato1(String idtabla, String dato1)");
             }
         }catch (Exception e) {
                em.clear();
                em.close();
                System.out.println("Error en BuscarIdtablaDato1 de TablasService: " + e.getMessage());
                return listaProducto;
        }
        em.clear();
        em.close();
        return listaProducto;
    
    }

}
