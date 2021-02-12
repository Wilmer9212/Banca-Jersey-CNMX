
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
        int pageNumber = 1;
        int pageSize = 10;
        Query query = em.createNativeQuery("SELECT * FROM personas");
        query.setFirstResult((pageNumber - 1) * pageSize);
        query.setMaxResults(pageSize);
        List<Persona> fooList = fooList = query.getResultList();
        System.out.println("Lista:" + fooList.size());

    }

}
