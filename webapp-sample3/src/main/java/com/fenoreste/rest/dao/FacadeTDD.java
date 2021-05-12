/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fenoreste.rest.dao;

import com.fenoreste.rest.Util.AbstractFacade;
import com.fenoreste.rest.entidades.Tablas;
import com.fenoreste.rest.entidades.TablasPK;
import com.syc.services.SiscoopTDD;
import com.syc.ws.endpoint.siscoop.BalanceQueryResponseDto;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Elliot
 */


//MetodosImplementadosDeSyC
public abstract class FacadeTDD<T> {

    Calendar calendar = Calendar.getInstance();
    Date hoy = calendar.getTime();
    private static EntityManagerFactory emf;

    public FacadeTDD(Class<T> entityClass) {
        emf = AbstractFacade.conexion();
    }
    
    public BalanceQueryResponseDto balanceQuery(String pan){
        
        EntityManager em=emf.createEntityManager();
        double saldo;
        try {
            TablasPK pk=new TablasPK("siscoop_banca_movil","wsdl_parametros");
            Tablas tb=em.find(Tablas.class, pk); 
            
            if(authSyC(tb.getDato1(),tb.getDato2())){
                SiscoopTDD tdd=new SiscoopTDD(tb.getDato1(),tb.getDato2());
                BalanceQueryResponseDto dto=tdd.getSiscoop().getBalanceQuery(pan);
                return dto;
            }
        } catch (Exception e) {
            System.out.println("Error al consultar saldo de TDD:"+e.getMessage());
        }
        return null;
    }
  
    public boolean authSyC(String user,String pass){
        System.out.println("llego a auth");
        System.out.println("user:"+user+",pass:"+pass);
        boolean bandera=true;
        try {                       
            System.out.println("entro a try");
            SiscoopTDD syc=new SiscoopTDD(user,pass);
            System.out.println("salio");
            bandera=true;
        } catch (Exception e) {
            System.out.println("Error al autenticar:"+e.getMessage());
        }
        System.out.println("fin");
        return bandera;
    }
    
       public void cerrar() {
        emf.close();
    }
}