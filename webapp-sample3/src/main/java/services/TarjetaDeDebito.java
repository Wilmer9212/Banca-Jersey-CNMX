/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.syc.ws.endpoint.siscoop.BalanceQueryResponseDto;

/**
 *
 * @author Elliot
 */
public class TarjetaDeDebito {
    
    
        // CONSULTA Y ACTUALIZA EL SALDO DE LA TarjetaDeDebito
    public BalanceQueryResponseDto saldoTDD(SaldoTddPK saldoTddPK, int idusuario) {
        BalanceQueryResponseDto balanceQueryResponseDto = new BalanceQueryResponseDto();
        return null;
        /*
        WsSiscoopFoliosTarjetasDTO tarjeta =  wsSiscoopFoliosTarjetasService.buscaTarjetaTDD(saldoTddPK.getIdorigenp(), saldoTddPK.getIdproducto(), saldoTddPK.getIdauxiliar());
        System.out.println("Llegando al web service de SYC");
        if (tarjeta.getWsSiscoopFoliosTarjetasPK() != null) {
            System.out.println("entro al if 1");
            // La tarjeta tiene que estar activa
            if (tarjeta.getActiva()) {
                System.out.println("entro al if 2");
                try {
                    System.out.println("idtjeta:"+tarjeta.getWsSiscoopFoliosTarjetasPK().getIdtarjeta());
                    balanceQueryResponseDto = siscoopTDD.getSiscoop().getBalanceQuery(tarjeta.getWsSiscoopFoliosTarjetasPK().getIdtarjeta());
                    System.out.println("Descripcion:"+balanceQueryResponseDto.getDescription());
                    System.out.println("Disponible:"+balanceQueryResponseDto.getAvailableAmount());
                    if (balanceQueryResponseDto.getCode() == 1) {
                        System.out.println("entro al if de code=1");
                        int actualizaSaldoTdd = saldoTddService.actualizaSaldoTdd(saldoTddPK, new BigDecimal(balanceQueryResponseDto.getAvailableAmount()), saiFunciones.saiFechaDB("24"));
                        if (actualizaSaldoTdd == 0) {
                            System.out.println("entro al if de saldo=0");
                            System.out.println("Error no se actualizo el saldo de la tarjeta TDD en la tabla saldo_tdd. ");
                            balanceQueryResponseDto.setCode(0);
                            return balanceQueryResponseDto;
                        }
                    }
                    if (!BalanceQueryResponse(balanceQueryResponseDto, tarjeta, idusuario)) {
                        System.out.println("Error no se inserto el registro en el log de TDD, tabla ws_siscoop_resultado_final_bancamovil. ");
                    }
                } catch (Exception e) {
                    balanceQueryResponseDto.setDescription("Connect timed out");
                    BalanceQueryResponse(balanceQueryResponseDto, tarjeta, idusuario);
                    System.out.println("Error al consultar SYC, tiempo agotado. " + e.getMessage());
                   
                }
            } else {
                System.out.println("hasta aqui");
                //balanceQueryResponseDto.setCode(0);
                balanceQueryResponseDto.setDescription("La tarjeta esta inactiva: " + tarjeta.getWsSiscoopFoliosTarjetasPK().getIdtarjeta());
            }
        }
        System.out.println("responseDTO:"+balanceQueryResponseDto);
        return balanceQueryResponseDto;
    }
  */
    }
    
}
