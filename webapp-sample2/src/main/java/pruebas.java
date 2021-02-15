
import com.fenoreste.ws.rest.Util.AbstractFacade;
import java.util.Formatter;
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
 * @author wilmer
 */

public class pruebas {
    public static void main(String[] args) {
        EntityManagerFactory emf=AbstractFacade.conexion();
        EntityManager em=emf.createEntityManager();
        try{
       /* Auxiliares_dPK dpk=new Auxiliares_dPK(30506,110,4470);
        AuxiliaresD d=em.find(AuxiliaresD.class,dpk);*/
        //System.out.println(" IdProducto:"+d.getPeriodo());
        }catch(Exception e){
            System.out.println("Error:"+e.getStackTrace());
        }
        emf.close();
    }
    
}




