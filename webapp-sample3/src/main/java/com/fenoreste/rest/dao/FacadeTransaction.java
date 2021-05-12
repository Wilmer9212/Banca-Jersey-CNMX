/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fenoreste.rest.dao;

import com.fenoreste.rest.ResponseDTO.TransactionToOwnAccountsDTO;
import com.fenoreste.rest.Util.AbstractFacade;
import com.fenoreste.rest.entidades.Auxiliares;
import com.fenoreste.rest.entidades.Transfers;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 *
 * @author Elliot
 */
public abstract class FacadeTransaction<T> {
    EntityManagerFactory emf;

    public FacadeTransaction(Class<T> entityClass) {
        emf = AbstractFacade.conexion();
    }

    
    public String[] transferenciasEtreMisCuentas(TransactionToOwnAccountsDTO transactionOWN){
        EntityManager em=emf.createEntityManager();
        Date hoy=new Date();
        String[]arr=new String[2];
        try {
         Transfers transaction=new Transfers();
         boolean bandera=false;
         boolean bandera2=false;
         EntityTransaction tr=em.getTransaction();
         tr.begin();
         transaction.setSubtransactiontypeid(transactionOWN.getSubTransactionTypeId());
         transaction.setCurrencyid(transactionOWN.getCurrencyId());
         transaction.setValuedate(transactionOWN.getValueDate());
         transaction.setTransactiontypeid(transactionOWN.getTransactionTypeId());
         transaction.setTransactionstatusid(transactionOWN.getTransactionStatusId());
         transaction.setClientbankidentifier(transactionOWN.getClientBankIdentifier());
         transaction.setDebitproductbankidentifier(transactionOWN.getDebitProductBankIdentifier());
         transaction.setDebitproducttypeid(transactionOWN.getDebitProductTypeId());
         transaction.setDebitcurrencyid(transactionOWN.getDebitCurrencyId());
         transaction.setCreditproductbankidentifier(transactionOWN.getCreditProductBankIdentifier());
         transaction.setCreditproducttypeid(transactionOWN.getCreditProductTypeId());
         transaction.setCreditcurrencyid(transactionOWN.getCreditCurrencyId());
         transaction.setAmount(transactionOWN.getAmount());
         transaction.setNotifyto(transactionOWN.getNotifyTo());
         transaction.setNotificationchannelid(transactionOWN.getNotificationChannelId());
         transaction.setTransactionid(transactionOWN.getTransactionId());
         transaction.setDestinationname(transactionOWN.getDestinationName());
         transaction.setDestinationbank(transactionOWN.getDestinationBank());
         transaction.setDescription(transactionOWN.getDescription());
         transaction.setBankroutingnumber(transactionOWN.getBankRoutingNumber());
         transaction.setSourcename(transactionOWN.getSourceName());
         transaction.setSourcebank(transactionOWN.getSourceBank());
         transaction.setRegulationamountexceeded(transactionOWN.isRegulationAmountExceeded());
         transaction.setSourcefunds(transactionOWN.getSourceFunds());
         transaction.setDestinationfunds(transactionOWN.getDestinationFunds());
         transaction.setTransactioncost(transactionOWN.getTransactionCost());
         transaction.setTransactioncostcurrencyid(transactionOWN.getTransactionCostCurrencyId());
         transaction.setExchangerate(transactionOWN.getExchangeRate());
         transaction.setFechaejecucion(hoy);
         
         em.persist(transaction);
         bandera=true;        
         
         tr.commit();
         
         if(bandera){
           EntityTransaction tr1=em.getTransaction();
           tr1.begin();
           em.createNativeQuery("UPDATE auxiliares a SET saldo="
           + "(SELECT saldo FROM auxiliares WHERE "
                    + "replace(to_char(idorigenp,'099999')||to_char(idproducto,'09999')||to_char(idauxiliar,'09999999'),' ','')='"+transaction.getDebitproductbankidentifier()+"')-"+transaction.getAmount()
           + " WHERE replace(to_char(idorigenp,'099999')||to_char(idproducto,'09999')||to_char(idauxiliar,'09999999'),' ','')='"+transaction.getDebitproductbankidentifier()+"'").executeUpdate();
          tr1.commit();
          
           EntityTransaction tr2=em.getTransaction();
           tr2.begin();
           em.createNativeQuery("UPDATE auxiliares a SET saldo="
           + "(SELECT saldo FROM auxiliares WHERE "
                    + "replace(to_char(idorigenp,'099999')||to_char(idproducto,'09999')||to_char(idauxiliar,'09999999'),' ','')='"+transaction.getCreditproductbankidentifier()+"')+"+transaction.getAmount()
           + " WHERE replace(to_char(idorigenp,'099999')||to_char(idproducto,'09999')||to_char(idauxiliar,'09999999'),' ','')='"+transaction.getCreditproductbankidentifier()+"'").executeUpdate();
          tr2.commit();
          bandera2=true;
         }
         
         String iserror="",idtransaction="";
         if(bandera2){
             iserror="false";
             idtransaction=String.valueOf(transaction.getTransactionid());
             arr[0]=iserror;
             arr[1]=idtransaction;
         }else{
             iserror="true";
             arr[0]=iserror;
             arr[1]=idtransaction;   
         }
        } catch (Exception e) {
            System.out.println("Error al insertar registro:"+e.getMessage());
        }
        em.close();
        return arr;
    }
   
  //Buscar si existe la cuenta,si pertenece al socio,si tiene saldo,si esta activa y si la cuenta a la que se esta transfiriendo realmente es del mismo socio
  public boolean buscar(String opa,String customerId,Double saldo,String opa2){
      EntityManager em=emf.createEntityManager();
      boolean bandera=false;
      try {
       String consulta="SELECT * FROM auxiliares WHERE replace(to_char(idorigenp,'099999')||to_char(idproducto,'09999')||to_char(idauxiliar,'09999999'),' ','')='"+opa
                      +"' AND replace(to_char(idorigen,'099999')||to_char(idgrupo,'09')||to_char(idsocio,'099999'),' ','')='"+customerId
                      +"' AND saldo>="+saldo+" AND estatus=2";
       Query query=em.createNativeQuery(consulta,Auxiliares.class);
       
       Auxiliares a=(Auxiliares) query.getSingleResult();
       String consulta2="SELECT * FROM auxiliares WHERE idorigen="+a.getIdorigen()+
                        " AND idgrupo="+a.getIdgrupo()+
                        " AND idsocio="+a.getIdsocio()+
                        " AND estatus=2"+
                        " AND replace(to_char(idorigenp,'099999')||to_char(idproducto,'09999')||to_char(idauxiliar,'09999999'),' ','')='"+opa2+"'";
       Query aux=em.createNativeQuery(consulta2,Auxiliares.class);
       Auxiliares aa=(Auxiliares) aux.getSingleResult();
       if(query!=null && aux!=null){
           bandera=true;
       }
      } catch (Exception e) {
          System.out.println("Error al validar la transferencia:"+e.getMessage());
      }
      em.close();
      System.out.println(" bandera:"+bandera);
      return bandera;
  }    
}
