
import com.fenoreste.ws.rest.Util.AbstractFacade;
import com.fenoreste.ws.rest.modelos.entidad.Grupos;
import com.fenoreste.ws.rest.modelos.entidad.Persona;
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
public class TestPagination {

    public static void main(String[] args) {
        EntityManagerFactory emf = AbstractFacade.conexion();
        EntityManager em = emf.createEntityManager();
        int pageNumber = 2;
        int pageSize = 10;
        
         int tamanyoPagina = 10;
         int paginaAMostrar = 3;         
         int inicioB=0;
         
         Query query = em.createNativeQuery("SELECT * FROM personas order by idsocio ASC",Persona.class);
         query.setMaxResults(tamanyoPagina);
         if(paginaAMostrar==1){
             inicioB=paginaAMostrar;
         }else if(paginaAMostrar>1){
              inicioB=((paginaAMostrar * tamanyoPagina)-tamanyoPagina);
         }
        System.out.println("Inicio:"+inicioB);
        query.setFirstResult(inicioB);
        
        Query query1 = em.createNativeQuery("SELECT * FROM personas order by idsocio ASC",Persona.class);
        query1.setMaxResults(10);
        query1.setFirstResult(20);
        
        List<Persona> fooList =query.getResultList();
        List<Persona> fooList1 =query1.getResultList();
        for(int i=0;i<fooList.size();i++){
            System.out.println("Nombre:"+fooList.get(i).getPersonasPK().getIdsocio()+" " +fooList.get(i).getNombre()+" "+fooList.get(i).getAppaterno() +" "+fooList.get(i).getApmaterno());
        }
        
        System.out.println("========================================================================================");
        for(int ii=0;ii<fooList1.size();ii++){
            System.out.println("Nombre1:"+fooList1.get(ii).getPersonasPK().getIdsocio()+" " +fooList1.get(ii).getNombre()+" "+fooList1.get(ii).getAppaterno() +" "+fooList1.get(ii).getApmaterno());
        }
        emf.close();
    }

}
