
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
        Query query=em.createNativeQuery("SELECT nombre FROM personas limit 1");
        String nombre=(String)query.getSingleResult();
        System.out.println("El nombre es:"+nombre);
        emf.close();
    }
    
}




