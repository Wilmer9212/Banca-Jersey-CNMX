
import com.fenoreste.ws.rest.Bankingly.dto.GetAccountMovementsDTO;
import com.fenoreste.ws.rest.Util.AbstractFacade;
import com.fenoreste.ws.rest.modelos.entidad.AuxiliaresD;
import com.fenoreste.ws.rest.modelos.entidad.Productos;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Elliot
 */
public class PaginationMethod {
    
    public static void main(String[] args) {
        getAccountMovements("0305060011000004470","","",10,0);
    }

    public static List<GetAccountMovementsDTO> getAccountMovements(String productBankIdentifier, String dateFromFilter, String dateToFilter, int pageSize, int pageStartIndex) {
        EntityManagerFactory emf=AbstractFacade.conexion();
        EntityManager em = emf.createEntityManager();
        GetAccountMovementsDTO cuenta;
        boolean isDC = false;
        String Description = "";
        List<GetAccountMovementsDTO> ListaDTO = new ArrayList<GetAccountMovementsDTO>();

        int pageNumber = pageStartIndex;
        int pageSizes = pageSize;
        int inicioB = 0;

        //Query query = em.createNativeQuery("SELECT * FROM personas order by idsocio ASC",Persona.class);
        String consulta = "";
        if (!dateFromFilter.equals("") && !dateToFilter.equals("")) {
            consulta = " SELECT *"
                    + "         FROM auxiliares_d"
                    + "         WHERE date(fecha) between '" + dateFromFilter + "'"
                    + "         AND '" + dateToFilter + "' AND replace((to_char(idorigenp,'099999')||to_char(idproducto,'09999')||to_char(idauxiliar,'09999999')),' ','')='" + productBankIdentifier + "' ORDER BY fecha ASC";
        } else if (!dateFromFilter.equals("") && dateToFilter.equals("")) {
            consulta = " SELECT *"
                    + "         FROM auxiliares_d"
                    + "         WHERE date(fecha) > '" + dateFromFilter + "' AND replace((to_char(idorigenp,'099999')||to_char(idproducto,'09999')||to_char(idauxiliar,'09999999')),' ','')='" + productBankIdentifier + "' ORDER BY fecha ASC";

        } else if (dateFromFilter.equals("") && !dateToFilter.equals("")) {
            consulta = " SELECT *"
                    + "         FROM auxiliares_d"
                    + "         WHERE date(fecha) < '" + dateToFilter + "' AND replace((to_char(idorigenp,'099999')||to_char(idproducto,'09999')||to_char(idauxiliar,'09999999')),' ','')='" + productBankIdentifier + "' ORDER BY fecha ASC";

        } else {
            consulta = " SELECT *"
                    + "         FROM auxiliares_d"
                    + "         WHERE replace((to_char(idorigenp,'099999')||to_char(idproducto,'09999')||to_char(idauxiliar,'09999999')),' ','')='" + productBankIdentifier + "' ORDER BY fecha ASC";

        }

        System.out.println("Consulta:" + consulta);

        if (pageNumber == 1 || pageNumber == 0) {
            System.out.println("Si");
            inicioB = pageNumber;
        } else if (pageNumber > 1) {
            inicioB = ((pageNumber * pageSizes) - pageSizes);
        }

        try {
            System.out.println("Entrando con inicio B:" + inicioB);
            Query queryE = em.createNativeQuery(consulta);
            queryE.setFirstResult(0);
            queryE.setMaxResults(pageSizes);
            List<Object[]>Listaa=queryE.getResultList();   
            for(Object[] obj:Listaa) {                   
            GetAccountMovementsDTO dto=new GetAccountMovementsDTO();
                dto.setAccountBankIdentifier(productBankIdentifier);
                dto.setMovementDate(obj[3].toString());
                dto.setCheckId("");
                dto.setAmount(Double.parseDouble(obj[5].toString()));
                dto.isIsDebit();
                dto.setMovementTypeId(1);
                dto.setTypeDescription("Abono");
                dto.setCheckId("hol");
                dto.setVoucherId(obj[12].toString());                
                System.out.println("DTO:"+dto);
                ListaDTO.add(dto);
                
                
                /*if (ad.getCargoabono() == 1) {
                    Description = "Abono";
                } else if (ad.getCargoabono() == 0) {
                    Description = "Cargo";
                }*/
                //Productos productos = em.find(Productos.class, ad.getAuxiliaresDPK().getIdproducto());
               /* if (productos.getTipoproducto() == 2) {
                    isDC = false;
                } else {
                    isDC = true;
                }*/
               /* GetAccountMovementsDTO dto = new GetAccountMovementsDTO(
                        ad.getIdpoliza(),
                        productBankIdentifier,
                        ad.getFecha().toString(),
                        Description,
                        Double.parseDouble(ad.getMonto().toString()),
                        isDC,
                        Double.parseDouble(ad.getSaldoec().toString()),
                        ad.getIdpoliza(),
                        Description,
                        ad.getIdtipo().toString(),
                        ad.getIdtipo().toString());   
                ListaDTO.add(dto);
                
            */ 
               
            }
            System.out.println("ListaDto.Size:"+ListaDTO.size());
            for(int h=0;h<ListaDTO.size();h++){
                System.out.println("Fecha:"+ListaDTO.get(h).getMovementDate());
            }
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        }finally{
            em.clear();
            em.close();
            emf.close();
        }
        
        return ListaDTO;
    }

}
