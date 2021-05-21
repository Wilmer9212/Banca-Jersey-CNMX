/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fenoreste.rest.WsTDD;

import com.fenoreste.rest.Util.AbstractFacade;
import com.fenoreste.rest.entidades.WsFoliosTarjetasSyC1;
import com.fenoreste.rest.entidades.WsFoliosTarjetasSyCPK1;
import com.syc.ws.endpoint.siscoop.BalanceQueryResponseDto;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Elliot
 */
public class TarjetaDeDebito {
    
    EntityManagerFactory emf=AbstractFacade.conexion();
   
    
    SiscoopTDD SiscoopTdd;
        // CONSULTA Y ACTUALIZA EL SALDO DE LA TarjetaDeDebito
    public BalanceQueryResponseDto saldoTDD(WsFoliosTarjetasSyCPK1 saldotTddPK) {
         EntityManager em=emf.createEntityManager();
        BalanceQueryResponseDto balanceQueryResponseDto = new BalanceQueryResponseDto();
        WsFoliosTarjetasSyC1 tarjeta =  em.find(WsFoliosTarjetasSyC1.class,saldotTddPK);
        System.out.println("Llegando al web service de SYC");
        try{
        if (tarjeta.getWsSiscoopFoliosTarjetasPK() != null) {
            System.out.println("entro al if 1");
            // La tarjeta tiene que estar activa
            if (tarjeta.getActiva()) {
                System.out.println("entro al if 2");
                try {
                    System.out.println("idtjeta:"+tarjeta.getIdtarjeta());
                    balanceQueryResponseDto = SiscoopTdd.getSiscoop().getBalanceQuery(tarjeta.getIdtarjeta());
                    System.out.println("Descripcion:"+balanceQueryResponseDto.getDescription());
                    System.out.println("Disponible:"+balanceQueryResponseDto.getAvailableAmount());
                    System.out.println("Code balance:"+balanceQueryResponseDto.getCode());
                    if (balanceQueryResponseDto.getCode() == 1) {
                        System.out.println("entro al if de code=1");
                        /*int actualizaSaldoTdd = saldoTddService.actualizaSaldoTdd(saldoTddPK, new BigDecimal(balanceQueryResponseDto.getAvailableAmount()), saiFunciones.saiFechaDB("24"));
                        if (actualizaSaldoTdd == 0) {
                            System.out.println("entro al if de saldo=0");
                            System.out.println("Error no se actualizo el saldo de la tarjeta TDD en la tabla saldo_tdd. ");
                            balanceQueryResponseDto.setCode(0);
                            return balanceQueryResponseDto;
                        }*/
                    }
                    /*if (!BalanceQueryResponse(balanceQueryResponseDto, tarjeta, idusuario)) {
                        System.out.println("Error no se inserto el registro en el log de TDD, tabla ws_siscoop_resultado_final_bancamovil. ");
                    }*/
                } catch (Exception e) {
                    balanceQueryResponseDto.setDescription("Connect timed out");
                    System.out.println("Error al consultar SYC, tiempo agotado. " + e.getMessage());
                    em.close();
                    emf.close();
                    
                }
            } else {
                System.out.println("hasta aqui");
                //balanceQueryResponseDto.setCode(0);
                balanceQueryResponseDto.setDescription("La tarjeta esta inactiva: " + tarjeta.getIdtarjeta());
            }
            em.close();
            emf.close();
        }
        }catch(Exception e){
            em.close();
            emf.close();
            System.out.println("Error producido en tdd:"+e.getMessage());
        }finally{
        em.clear();
        em.close();
        emf.close();
        }
       
        System.out.println("responseDTO:"+balanceQueryResponseDto);
        return balanceQueryResponseDto;
    }
  
    }
    
