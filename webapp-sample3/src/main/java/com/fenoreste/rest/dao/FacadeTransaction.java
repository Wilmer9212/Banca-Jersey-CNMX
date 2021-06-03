/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fenoreste.rest.dao;

import com.fenoreste.rest.ResponseDTO.BackendOperationResultDTO;
import com.fenoreste.rest.ResponseDTO.TransactionToOwnAccountsDTO;
import com.fenoreste.rest.Util.AbstractFacade;
import com.fenoreste.rest.entidades.Auxiliares;
import com.fenoreste.rest.entidades.Transfers;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import sun.swing.BakedArrayList;

/**
 *
 * @author Elliot
 */
public abstract class FacadeTransaction<T> {

    EntityManagerFactory emf;

    public FacadeTransaction(Class<T> entityClass) {
        emf = AbstractFacade.conexion();
    }

    public BackendOperationResultDTO transferencias(TransactionToOwnAccountsDTO transactionOWN) {
        EntityManager em = emf.createEntityManager();
        Date hoy = new Date();
        String[] arr = new String[2];
        BackendOperationResultDTO backendResult = new BackendOperationResultDTO();
        String messageBackend=comprobarEntreMisCuentas(transactionOWN.getDebitProductBankIdentifier(),
                                                       transactionOWN.getCreditProductBankIdentifier(),
                                                       transactionOWN.getAmount()).toUpperCase();
        System.out.println("BackendMessage:"+messageBackend);
        try {
            if(messageBackend.contains("EXITO")){
            Transfers transaction = new Transfers();            
            boolean bandera = false;
            boolean bandera2 = false;
            EntityTransaction tr = em.getTransaction();
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
            bandera = true;

            tr.commit();

            if (bandera) {
                em.getTransaction().begin();
                em.createNativeQuery("UPDATE auxiliares a SET saldo="
                        + "(SELECT saldo FROM auxiliares WHERE "
                        + "replace(to_char(idorigenp,'099999')||to_char(idproducto,'09999')||to_char(idauxiliar,'09999999'),' ','')='" + transaction.getDebitproductbankidentifier() + "')-" + transaction.getAmount()
                        + " WHERE replace(to_char(idorigenp,'099999')||to_char(idproducto,'09999')||to_char(idauxiliar,'09999999'),' ','')='" + transaction.getDebitproductbankidentifier() + "'").executeUpdate();

                em.createNativeQuery("UPDATE auxiliares a SET saldo="
                        + "(SELECT saldo FROM auxiliares WHERE "
                        + "replace(to_char(idorigenp,'099999')||to_char(idproducto,'09999')||to_char(idauxiliar,'09999999'),' ','')='" + transaction.getCreditproductbankidentifier() + "')+" + transaction.getAmount()
                        + " WHERE replace(to_char(idorigenp,'099999')||to_char(idproducto,'09999')||to_char(idauxiliar,'09999999'),' ','')='" + transaction.getCreditproductbankidentifier() + "'").executeUpdate();
                em.getTransaction().commit();
                bandera2 = true;
            }
            System.out.println("paso");
            String iserror = "", idtransaction = "";

            if (bandera2) {
                idtransaction = String.valueOf(transaction.getTransactionid());
                backendResult.setIsError(false);
                backendResult.setBackendCode("1");
                backendResult.setBackendMessage(messageBackend);
                backendResult.setIntegrationProperties("{}");
                backendResult.setBackendReference(null);
                backendResult.setTransactionIdenty(idtransaction);
            }
            }else {
                backendResult.setIsError(true);
                backendResult.setBackendCode("2");
                backendResult.setBackendMessage(messageBackend);
                backendResult.setIntegrationProperties("{}");
                backendResult.setBackendReference(null);
                backendResult.setTransactionIdenty("0");
                System.out.println("aaaaa");
                return backendResult;
            }
        } catch (Exception e) {
            backendResult.setIsError(true);
            backendResult.setBackendCode("2");
            backendResult.setBackendMessage(messageBackend);
            backendResult.setIntegrationProperties("{}");
            backendResult.setBackendReference(null);
            backendResult.setTransactionIdenty("0");
            em.close();
            System.out.println("Error al insertar registro:" + e.getMessage());
            return backendResult;
        }
        em.close();
        return backendResult;
    }

    public BackendOperationResultDTO PageToPrestamo(TransactionToOwnAccountsDTO transactionOWN) {
        EntityManager em = emf.createEntityManager();
        Date hoy = new Date();
         String backendMessage="";
        BackendOperationResultDTO backendResult = new BackendOperationResultDTO();
        if(transactionOWN.getSubTransactionTypeId()==1){
        backendMessage=comprobarEntreMisCuentas(transactionOWN.getDebitProductBankIdentifier(),
                                                      transactionOWN.getCreditProductBankIdentifier(),
                                                      transactionOWN.getAmount());  
        }
        
        try {
            Transfers transaction = new Transfers();
            boolean bandera = false;
            boolean bandera2 = false;
            if(backendMessage.toUpperCase().contains("EXITO")){
            EntityTransaction tr = em.getTransaction();
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
            bandera = true;          
            tr.commit();
            
            }
            if (bandera) {
                em.getTransaction().begin();
                em.createNativeQuery("UPDATE auxiliares a SET saldo="
                        + "(SELECT saldo FROM auxiliares WHERE "
                        + "replace(to_char(idorigenp,'099999')||to_char(idproducto,'09999')||to_char(idauxiliar,'09999999'),' ','')='" + transaction.getDebitproductbankidentifier() + "')-" + transaction.getAmount()
                        + " WHERE replace(to_char(idorigenp,'099999')||to_char(idproducto,'09999')||to_char(idauxiliar,'09999999'),' ','')='" + transaction.getDebitproductbankidentifier() + "'").executeUpdate();
                
                em.createNativeQuery("UPDATE auxiliares a SET saldo="
                        + "(SELECT saldo FROM auxiliares WHERE "
                        + "replace(to_char(idorigenp,'099999')||to_char(idproducto,'09999')||to_char(idauxiliar,'09999999'),' ','')='" + transaction.getCreditproductbankidentifier() + "')-" + transaction.getAmount()
                        + " WHERE replace(to_char(idorigenp,'099999')||to_char(idproducto,'09999')||to_char(idauxiliar,'09999999'),' ','')='" + transaction.getCreditproductbankidentifier() + "'").executeUpdate();
                em.getTransaction().commit();
                bandera2 = true;
            }
            String idtransaction = "";
            if (bandera2) {
                idtransaction = String.valueOf(transaction.getTransactionid());
                backendResult.setIsError(false);
                backendResult.setBackendCode("1");
                backendResult.setBackendMessage(backendMessage);
                backendResult.setIntegrationProperties("{}");
                backendResult.setBackendReference(null);
                backendResult.setTransactionIdenty(idtransaction);
            } else {
                backendResult.setIsError(true);
                backendResult.setBackendCode("2");
                backendResult.setBackendMessage(backendMessage);
                backendResult.setIntegrationProperties("{}");
                backendResult.setBackendReference(null);
                backendResult.setTransactionIdenty("0");
            }
        } catch (Exception e) {
            backendResult.setIsError(true);
            backendResult.setBackendCode("2");
            backendResult.setBackendMessage(backendMessage);
            backendResult.setIntegrationProperties("{}");
            backendResult.setBackendReference(null);
            backendResult.setTransactionIdenty("0");
            em.close();
            System.out.println("Error al insertar registro:" + e.getMessage());
            return backendResult;
        }
        em.close();
        return backendResult;
    }

    //Buscar si existe la cuenta,si pertenece al socio,si tiene saldo,si esta activa y si la cuenta a la que se esta transfiriendo realmente es del mismo socio
    public boolean buscarEntreMisCuentas(String cuentaOrigen, String customerId, Double saldo, String cuentaDestino) {
        EntityManager em = emf.createEntityManager();
        boolean bandera = false;
        try {
            String consulta = "SELECT * FROM auxiliares a WHERE replace(to_char(a.idorigenp,'099999')||to_char(a.idproducto,'09999')||to_char(a.idauxiliar,'09999999'),' ','')='" + cuentaOrigen
                    + "' AND replace(to_char(a.idorigen,'099999')||to_char(a.idgrupo,'09')||to_char(a.idsocio,'099999'),' ','')='" + customerId
                    + "' AND a.saldo>=" + saldo + " AND a.estatus=2 AND (SELECT tipoproducto FROM productos pr WHERE pr.idproducto=a.idproducto)!=2";
            Query query = em.createNativeQuery(consulta, Auxiliares.class);

            Auxiliares a = (Auxiliares) query.getSingleResult();
            String consulta2 = "SELECT * FROM auxiliares a WHERE a.idorigen=" + a.getIdorigen()
                    + " AND a.idgrupo=" + a.getIdgrupo()
                    + " AND a.idsocio=" + a.getIdsocio()
                    + " AND a.estatus=2 "
                    + " AND replace(to_char(a.idorigenp,'099999')||to_char(a.idproducto,'09999')||to_char(a.idauxiliar,'09999999'),' ','')='" + cuentaDestino + "'"
                    + " AND (SELECT tipoproducto FROM productos pr WHERE pr.idproducto=a.idproducto)!=2";
            Query aux = em.createNativeQuery(consulta2, Auxiliares.class);
            Auxiliares aa = (Auxiliares) aux.getSingleResult();
            if (query != null && aux != null) {
                bandera = true;
            }
        } catch (Exception e) {
            em.close();
            System.out.println("Error al validar la transferencia:" + e.getMessage());
        }
        em.close();
        System.out.println(" bandera:" + bandera);
        return bandera;
    }

    public boolean buscarATerceros(String cuentaOrigen, String customerId, Double saldo, String cuentaDestino) {
        EntityManager em = emf.createEntityManager();
        boolean bandera = false;
        try {
            String consulta = "SELECT * FROM auxiliares a WHERE replace(to_char(a.idorigenp,'099999')||to_char(a.idproducto,'09999')||to_char(a.idauxiliar,'09999999'),' ','')='" + cuentaOrigen
                    + "' AND replace(to_char(a.idorigen,'099999')||to_char(a.idgrupo,'09')||to_char(a.idsocio,'099999'),' ','')='" + customerId
                    + "' AND a.saldo>=" + saldo + " AND a.estatus=2 AND (SELECT tipoproducto FROM productos pr WHERE pr.idproducto=a.idproducto)!=2";
            Query query = em.createNativeQuery(consulta, Auxiliares.class);

            Auxiliares a = (Auxiliares) query.getSingleResult();
            String consulta2 = "SELECT * FROM auxiliares a WHERE "
                    + " replace(to_char(a.idorigenp,'099999')||to_char(a.idproducto,'09999')||to_char(a.idauxiliar,'09999999'),' ','')='" + cuentaDestino + "'"
                    + " AND a.estatus=2 AND (SELECT tipoproducto FROM productos pr WHERE pr.idproducto=a.idproducto)!=2";

            Query aux = em.createNativeQuery(consulta2, Auxiliares.class);
            Auxiliares aa = (Auxiliares) aux.getSingleResult();
            if (query != null && aux != null) {
                bandera = true;
            }
        } catch (Exception e) {
            em.close();
            System.out.println("Error al validar la transferencia:" + e.getMessage());
        }
        em.close();
        System.out.println(" bandera:" + bandera);
        return bandera;
    }

    public boolean buscarPrestamos(String cuentaOrigen, String customerId, Double saldo, String cuentaDestino) {
        EntityManager em = emf.createEntityManager();
        boolean bandera = false;
        try {
            String consulta = "SELECT * FROM auxiliares a WHERE replace(to_char(a.idorigenp,'099999')||to_char(a.idproducto,'09999')||to_char(a.idauxiliar,'09999999'),' ','')='" + cuentaOrigen
                    + "' AND replace(to_char(a.idorigen,'099999')||to_char(a.idgrupo,'09')||to_char(a.idsocio,'099999'),' ','')='" + customerId
                    + "' AND a.saldo>=" + saldo + " AND a.estatus=2 AND (SELECT tipoproducto FROM productos pr WHERE pr.idproducto=a.idproducto)!=2";
            Query query = em.createNativeQuery(consulta, Auxiliares.class);

            Auxiliares a = (Auxiliares) query.getSingleResult();
            String consulta2 = "SELECT * FROM auxiliares a WHERE a.idorigen=" + a.getIdorigen()
                    + " AND a.idgrupo=" + a.getIdgrupo()
                    + " AND a.idsocio=" + a.getIdsocio()
                    + " AND a.estatus=2"
                    + " AND replace(to_char(a.idorigenp,'099999')||to_char(a.idproducto,'09999')||to_char(a.idauxiliar,'09999999'),' ','')='" + cuentaDestino + "'"
                    + " AND (SELECT tipoproducto FROM productos pr WHERE pr.idproducto=a.idproducto)=2";
            Query aux = em.createNativeQuery(consulta2, Auxiliares.class);
            Auxiliares aa = (Auxiliares) aux.getSingleResult();
            if (query != null && aux != null) {
                bandera = true;
            }
        } catch (Exception e) {
            em.close();
            System.out.println("Error al validar la transferencia:" + e.getMessage());
        }
        em.close();
        System.out.println(" bandera:" + bandera);
        return bandera;
    }

    public String BackendMessage(String opa, String ogs, Double Monto, String opa1, int tipoTransaccion) {
        String ErrorMessage = "";
        try {
            int op=tipoTransaccion;
            switch(op){
                case 1:
                    comprobarEntreMisCuentas(opa,opa1,Monto);
                    break;
                 
           
            }
        } catch (Exception e) {

        }
        return ErrorMessage;
    }
    
    //Busco si el saldo es mayor o igual al que se solicita para la transaccion
    public String comprobarEntreMisCuentas(String opa,String opa2, Double monto) {
        EntityManager em = emf.createEntityManager();
        String con = "SELECT saldo FROM auxiliares a WHERE replace(to_char(a.idorigenp,'099999')||to_char(a.idproducto,'09999')||to_char(a.idauxiliar,'09999999'),' ','')='" + opa + "'";
        String consulta2="SELECT estatus FROM auxiliares a WHERE replace(to_char(a.idorigenp,'099999')||to_char(a.idproducto,'09999')||to_char(a.idauxiliar,'09999999'),' ','')='" + opa2 + "'";
        String message = "";
        try {
            Query query = em.createNativeQuery(con);
            Double Saldo = Double.parseDouble(query.getSingleResult().toString());         
            if (Saldo >= monto) {
                 Query query2 = em.createNativeQuery(consulta2);
                          int estatus=Integer.parseInt(query2.getSingleResult().toString());
                          System.out.println("saldo:" + Saldo+", estatus:"+estatus);
                          if(estatus==2){
                              message="Trasaccion Exitosa";
                          }else{
                              message="Cuenta destino inactiva"; 
                          }
            } else {
             message="Fondos insuficientes para completar la transaccion";
            }
        } catch (Exception e) {
            em.close();
            message=e.getMessage();
            System.out.println("Error en revisar el saldo:" + e.getMessage());
        }
        em.close();
        return message.toUpperCase();
    }

    public void cerrar() {
        emf.close();
    }
}
