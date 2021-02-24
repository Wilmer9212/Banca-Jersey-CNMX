
import com.fenoreste.ws.rest.Util.AbstractFacade;
import com.fenoreste.ws.rest.modelos.entidad.Amortizaciones;
import com.fenoreste.ws.rest.modelos.entidad.AmortizacionesPK;
import com.fenoreste.ws.rest.modelos.entidad.AuxiliaresD;
import com.fenoreste.ws.rest.modelos.entidad.AuxiliaresDPK;
import com.fenoreste.ws.rest.modelos.entidad.Catalogo_Cuenta_Bankingly;
import com.fenoreste.ws.rest.modelos.entidad.Productos;
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
        /*AuxiliaresDPK dpk=new AuxiliaresDPK(30506,110,4470);
        AuxiliaresD d=em.find(AuxiliaresD.class,dpk);
        System.out.println(" IdProducto:"+d.getFecha());*/
        //Productos pr=em.find(Productos.class,110);
        //Catalogo_Cuenta_Bankingly ccb=em.find(Catalogo_Cuenta_Bankingly.class,110);
            AmortizacionesPK pk=new AmortizacionesPK(30505,30102,3003,1);
            Amortizaciones amor=em.find(Amortizaciones.class,pk);
            System.out.println("Amor:"+amor.getVence());
        }catch(Exception e){
            System.out.println("Error:"+e.getStackTrace());
            System.out.println("ErrorM:"+e.getMessage());
        }
        em.close();
        emf.close();
    }
    
}




