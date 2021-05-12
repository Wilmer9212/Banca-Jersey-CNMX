
import com.fenoreste.rest.Util.AbstractFacade;
import com.fenoreste.rest.entidades.AuxiliaresD;
import com.fenoreste.rest.entidades.AuxiliaresDPK;
import com.fenoreste.rest.entidades.WsFoliosTarjetasSyCPK;
import com.fenoreste.rest.entidades.WsFoliosTarjetasSyCPK1;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
public class pruebasGenerales {
    public static void main(String[] args) {
       /* EntityManagerFactory emf=AbstractFacade.conexion();
        EntityManager em=emf.createEntityManager();
        Query query =em.createNativeQuery("SELECT * FROM auxiliares_d WHERE "
                + "idorigenp="+30501+" AND idproducto="+30303+" AND idauxiliar="+1264,AuxiliaresD.class);
        List<AuxiliaresD>lista=query.getResultList();
        for(int i=0;i<lista.size();i++){
            AuxiliaresD ad=lista.get(i);
            System.out.println("ad:"+ad.getMonto());
        }
        em.close();*/
       
        WsFoliosTarjetasSyCPK1 pck=new WsFoliosTarjetasSyCPK1(1, 1, 0);
       int[]nu=new int[2];
       String[]cars={};
       nu[0]=1;
       nu[1]=2;
       
        System.out.println("nu:"+cars);
    }
    
    public static Date stringTodate(String fecha) {
        Date date = null;
        try {
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
            date = formato.parse(fecha);
        } catch (ParseException ex) {
            System.out.println("Error al convertir fecha:" + ex.getMessage());
        }
        System.out.println("date:" + date);
        return date;
    }
}
