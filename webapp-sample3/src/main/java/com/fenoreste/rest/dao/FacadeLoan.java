package com.fenoreste.rest.dao;

import com.fenoreste.rest.Util.AbstractFacade;
import com.fenoreste.rest.ResponseDTO.GetAccountLast5MovementsDTO;
import com.fenoreste.rest.ResponseDTO.GetAccountDetailsDTO;
import com.fenoreste.rest.ResponseDTO.GetAccountMovementsDTO;
import com.fenoreste.rest.ResponseDTO.LoanDTO;
import com.fenoreste.rest.entidades.Amortizaciones;
import com.fenoreste.rest.entidades.AmortizacionesPK;
import com.fenoreste.rest.entidades.AuxiliaresPK;
import com.fenoreste.rest.entidades.Productos;
import com.fenoreste.rest.entidades.Auxiliares;
import com.fenoreste.rest.entidades.AuxiliaresD;
import com.fenoreste.rest.entidades.Persona;
import java.math.BigDecimal;
import java.math.BigInteger;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

public abstract class FacadeLoan<T> {

    Calendar calendar = Calendar.getInstance();
    Date hoy = calendar.getTime();
    private static EntityManagerFactory emf;

    public FacadeLoan(Class<T> entityClass) {
        emf = AbstractFacade.conexion();
    }
    EntityManager em = null;

    public LoanDTO Loan(String productBankIdentifier,int feesStatus,int pageSize,int pageStartIndex) {
        em = emf.createEntityManager();
        int o = Integer.parseInt(productBankIdentifier.substring(0, 6));
        int p = Integer.parseInt(productBankIdentifier.substring(6, 11));
        int a = Integer.parseInt(productBankIdentifier.substring(11, 19));

        GetAccountDetailsDTO cuenta = null;
        System.out.println("O:" + o + ",P:" + p + ",A:" + a);
        try {
            AuxiliaresPK auxpk=new AuxiliaresPK(o,p,a);
            Auxiliares aux=em.find(Auxiliares.class,auxpk);
            String consulta="SELECT * FROM amortizaciones WHERE idorigenp="+o+" AND idproducto="+p+" AND idauxiliar="+a;
            Query queryAmortizaciones=em.createNativeQuery(consulta,Amortizaciones.class);
            List<Amortizaciones>ListaAmort=queryAmortizaciones.getResultList();
            System.out.println("Lista de amortizaciones:"+ListaAmort);
            System.out.println("consulta:"+consulta);
          
        } catch (Exception e) {
            cerrar();
            System.out.println("Error en GetAccountDetails:" + e.getMessage());
        }
        em.close();
        return null;//cuenta;

    }

    public List<GetAccountLast5MovementsDTO> getAccountLast5Movements(String accountId) {
        em = emf.createEntityManager();
        GetAccountLast5MovementsDTO cuenta;
        boolean isDC = false;
        String Description = "";
        List<GetAccountLast5MovementsDTO> ListaDTO = new ArrayList<GetAccountLast5MovementsDTO>();
        try {
            String consulta = " SELECT m.* "
                    + "         FROM auxiliares_d m"
                    + "         WHERE replace((to_char(idorigenp,'099999')||to_char(idproducto,'09999')||to_char(idauxiliar,'09999999')),' ','')= ? ORDER BY fecha DESC LIMIT 5";
            Query k = em.createNativeQuery(consulta);
            k.setParameter(1, accountId);

            List<Object[]> milista = k.getResultList();
            for (int i = 0; i < milista.size(); i++) {
                Object[] as = milista.get(i);
                if (Integer.parseInt(as[4].toString()) == 1) {
                    Description = "Abono";
                } else if (Integer.parseInt(as[4].toString()) == 0) {
                    Description = "Cargo";
                }
                Productos productos = em.find(Productos.class, Integer.parseInt(as[1].toString()));
                if (productos.getTipoproducto() == 2) {
                    isDC = false;
                } else {
                    isDC = true;
                }
                cuenta = new GetAccountLast5MovementsDTO(
                        Integer.parseInt(as[12].toString()),
                        accountId,
                        as[3].toString(),
                        Description,
                        Double.parseDouble(as[5].toString()),
                        isDC,
                        Double.parseDouble(as[14].toString()),
                        1,
                        Description,
                        as[12].toString(),
                        as[20].toString());

                ListaDTO.add(cuenta);
            }
            System.out.println("ListaDTO:" + ListaDTO);

        } catch (Exception e) {
            em.close();
            System.out.println("Error en GetAccountLast5Movements:" + e.getMessage());
        }
        return ListaDTO;
    }

    public List<GetAccountMovementsDTO> getAccountMovements(String productBankIdentifier, String dateFromFilter, String dateToFilter, int pageSize, int pageStartIndex) {
        em = emf.createEntityManager();
        GetAccountMovementsDTO cuenta;
        boolean isDC = false;
        String Description = "";
        List<GetAccountMovementsDTO> ListaDTO = new ArrayList<GetAccountMovementsDTO>();

        EntityManagerFactory emf = AbstractFacade.conexion();
        EntityManager em = emf.createEntityManager();
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
            inicioB = 0;
        } else if (pageNumber > 1) {
            inicioB = ((pageNumber * pageSizes) - pageSizes);
        }

        try {
            Query queryE = em.createNativeQuery(consulta);
            queryE.setFirstResult(inicioB);
            queryE.setMaxResults(pageSizes);
            List<Object[]> MiLista = queryE.getResultList();
            for(Object[] ListaO:MiLista) {
            if (Integer.parseInt(ListaO[4].toString()) == 1) {
                    Description = "Abono";
                } else if (Integer.parseInt(ListaO[4].toString()) == 0) {
                    Description = "Cargo";
                }
                Productos productos = em.find(Productos.class, Integer.parseInt(ListaO[1].toString()));
                if (productos.getTipoproducto() == 2) {
                    isDC = false;
                } else {
                    isDC = true;
                }
                GetAccountMovementsDTO dto = new GetAccountMovementsDTO(
                        Integer.parseInt(ListaO[12].toString()),
                        productBankIdentifier,
                        ListaO[3].toString(),
                        Description,
                        Double.parseDouble(ListaO[5].toString()),
                        isDC,
                        Double.parseDouble(ListaO[14].toString()),
                        Integer.parseInt(ListaO[12].toString()),
                        Description,
                        ListaO[11].toString(),
                        ListaO[11].toString());   
                ListaDTO.add(dto);
                
                
            }
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        }
        return ListaDTO;
    }

    public static Date substractDate(int numeroDias) {
        SimpleDateFormat d = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        Calendar calendar = Calendar.getInstance();
        Date fechaActual = calendar.getTime();

        //Si vamos a usar la fecha en tiempo real date=fechaActual
        //date = fechaActual;
        try {
            date = d.parse("31/12/2020");
        } catch (ParseException ex) {
            Logger.getLogger(FacadeLoan.class.getName()).log(Level.SEVERE, null, ex);
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, -numeroDias);
        return cal.getTime();
    }

    public static Date subtractDay24H() {
        SimpleDateFormat d = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        Calendar calendar = Calendar.getInstance();
        Date fechaActual = calendar.getTime();

        //Si vamos a usar la fecha en tiempo real date=fechaActual
        try {
            date = d.parse("'31/11/2021'");
        } catch (ParseException ex) {
            Logger.getLogger(FacadeLoan.class.getName()).log(Level.SEVERE, null, ex);
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    public static Date subtractIntervalMonth() {
        SimpleDateFormat d = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        Calendar calendar = Calendar.getInstance();
        Date fecha24H = calendar.getTime();
        try {
            date = d.parse("31/12/2020");
        } catch (ParseException ex) {
            Logger.getLogger(FacadeLoan.class.getName()).log(Level.SEVERE, null, ex);
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, -1);
        return cal.getTime();
    }

    public int contadorAuxD(String productBankIdentifier, String dateFromFilter, String dateToFilter) {
        String consulta = "";
        int count=0;
        try{
        if (!dateFromFilter.equals("") && !dateToFilter.equals("")) {
            consulta = " SELECT count(*)"
                    + "         FROM auxiliares_d"
                    + "         WHERE date(fecha) between '" + dateFromFilter + "'"
                    + "         AND '" + dateToFilter + "' AND replace((to_char(idorigenp,'099999')||to_char(idproducto,'09999')||to_char(idauxiliar,'09999999')),' ','')='" + productBankIdentifier + "'";
        } else if (!dateFromFilter.equals("") && dateToFilter.equals("")) {
            consulta = " SELECT count(*)"
                    + "         FROM auxiliares_d"
                    + "         WHERE date(fecha) > '" + dateFromFilter + "' AND replace((to_char(idorigenp,'099999')||to_char(idproducto,'09999')||to_char(idauxiliar,'09999999')),' ','')='" + productBankIdentifier + "'";

        } else if (dateFromFilter.equals("") && !dateToFilter.equals("")) {
            consulta = " SELECT count(*)"
                    + "         FROM auxiliares_d"
                    + "         WHERE date(fecha) < '" + dateToFilter + "' AND replace((to_char(idorigenp,'099999')||to_char(idproducto,'09999')||to_char(idauxiliar,'09999999')),' ','')='" + productBankIdentifier + "'";

        } else {
            consulta = " SELECT count(*)"
                    + "         FROM auxiliares_d"
                    + "         WHERE replace((to_char(idorigenp,'099999')||to_char(idproducto,'09999')||to_char(idauxiliar,'09999999')),' ','')='" + productBankIdentifier + "'";

        }
        Query query = em.createNativeQuery(consulta);
        BigInteger b1;
        b1 = new BigInteger(query.getSingleResult().toString());
        count = Integer.parseInt(b1.toString());
        }catch(Exception e){
        System.out.println("Error al contar registros:"+e.getMessage());
        }
        System.out.println("contadooooooooooooooooooooooooooor:"+count);
        em.close();
        return count;
    }

    /**
     * *********************************Cerrando conexiones
     * ***********************************
     */
    public void cerrar() {
        emf.close();
    }

}
