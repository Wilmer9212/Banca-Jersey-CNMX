package com.fenoreste.rest.dao;

import com.fenoreste.rest.ResponseDTO.FeesDueData;
import com.fenoreste.rest.Util.AbstractFacade;
import com.fenoreste.rest.ResponseDTO.AccountDetailsDTO;
import com.fenoreste.rest.ResponseDTO.AccountMovementsDTO;
import com.fenoreste.rest.ResponseDTO.LoanDTO;
import com.fenoreste.rest.ResponseDTO.LoanFee;
import com.fenoreste.rest.ResponseDTO.LoanFeeDTO;
import com.fenoreste.rest.entidades.Amortizaciones;
import com.fenoreste.rest.entidades.AuxiliaresPK;
import com.fenoreste.rest.entidades.Productos;
import com.fenoreste.rest.entidades.Auxiliares;
import com.fenoreste.rest.entidades.Catalog_Status_Bankingly;
import com.fenoreste.rest.entidades.Loan_Fee_Status;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

public abstract class FacadeLoan<T> {

    Calendar calendar = Calendar.getInstance();
    Date hoy = calendar.getTime();
    private static EntityManagerFactory emf;

    public FacadeLoan(Class<T> entityClass) {
        emf = AbstractFacade.conexion();
    }
    EntityManager em = null;

    public LoanDTO Loan(String productBankIdentifier) {
        em = emf.createEntityManager();
        int o = Integer.parseInt(productBankIdentifier.substring(0, 6));
        int p = Integer.parseInt(productBankIdentifier.substring(6, 11));
        int a = Integer.parseInt(productBankIdentifier.substring(11, 19));

        AccountDetailsDTO cuenta = null;
        System.out.println("O:" + o + ",P:" + p + ",A:" + a);
        LoanDTO dto = null;
        try {
            AuxiliaresPK auxpk = new AuxiliaresPK(o, p, a);
            Auxiliares aux = em.find(Auxiliares.class, auxpk);
            if(aux.getEstatus()==2){
            Catalog_Status_Bankingly sts = em.find(Catalog_Status_Bankingly.class, Integer.parseInt(aux.getEstatus().toString()));
            Double currentBalance = Double.parseDouble(aux.getSaldo().toString());
            Double currentRate = Double.parseDouble(aux.getTasaio().toString()) + Double.parseDouble(aux.getTasaim().toString()) + Double.parseDouble(aux.getTasaiod().toString());
            int loanStatusId = sts.getProductstatusid();
            
            Query query=em.createNativeQuery("SELECT count(*) FROM amortizaciones WHERE idorigenp="+o
                                           +" AND idproducto="+p
                                           +" AND idauxiliar="+a
                                           +" AND todopag=true");
            int amPag=Integer.parseInt(String.valueOf(query.getSingleResult()));
            
             Query query1=em.createNativeQuery("SELECT count(*) FROM amortizaciones WHERE idorigenp="+o
                                           +" AND idproducto="+p
                                           +" AND idauxiliar="+a);
            int tam=Integer.parseInt(String.valueOf(query1.getSingleResult()));
            System.out.println("Amortizaciones Pagadas:"+amPag);
            dto = new LoanDTO(
                    productBankIdentifier,
                    currentBalance,
                    currentRate,
                    RSFeesDue(o, p, a),
                    RSFeesDueData(o, p, a),//Campo FeesDueData
                    loanStatusId,
                    RSLoanFee(o, p, a),
                    Double.parseDouble(aux.getMontoprestado().toString()),
                    RSOverdueDays(o, p, a),
                    amPag,
                    0.0,
                    0.0,
                    productBankIdentifier,
                    tam,
                    true);
            System.out.println("LoanPre:"+dto);           
           }
        } catch (Exception e) {
            em.clear();
            em.close();
            System.out.println("Error en GetAccountDetails:" + e.getMessage());
        }
        em.close();
        return dto;//cuenta;

    }
    
    public LoanFee LoanFee(String productBankIdentifier,int feeNumber){
        EntityManager em = emf.createEntityManager();
        LoanFee loanFee = null;
        int o = Integer.parseInt(productBankIdentifier.substring(0, 6));
        int p = Integer.parseInt(productBankIdentifier.substring(6, 11));
        int a = Integer.parseInt(productBankIdentifier.substring(11, 19));

        
        try {
            AuxiliaresPK pk = new AuxiliaresPK(o, p, a);
            Auxiliares aux = em.find(Auxiliares.class, pk);
            //Obtengo informacion con el sai_auxiliar hasta la fecha actual, si hay dudas checar el catalogo o atributos que devuelve la funcion
            String sai_auxiliar = "SELECT * FROM sai_auxiliar(" + o + "," + p + "," + a + ",(SELECT date(fechatrabajo) FROM origenes limit 1))";
            Query RsSai = em.createNativeQuery(sai_auxiliar);
            String sai = RsSai.getSingleResult().toString();
            String[] parts = sai.split("\\|");
            List list = Arrays.asList(parts);
            //Obtengo la amortizacion que se vence
            String consultaA="SELECT * FROM amortizaciones WHERE idorigenp="+o
                        +" AND idproducto="+p
                        +" AND idauxiliar="+a
                        +" AND idorigenp+idproducto+idauxiliar+idamortizacion="+feeNumber;
            System.out.println("La consulta es:"+consultaA);
            Query queryA=em.createNativeQuery(consultaA,Amortizaciones.class);
            Amortizaciones amm=(Amortizaciones)queryA.getSingleResult();
            Double iovencido = Double.parseDouble(list.get(6).toString()) + Double.parseDouble(list.get(17).toString());
            Double imvencido = Double.parseDouble(list.get(15).toString()) + Double.parseDouble(list.get(18).toString());
            
            
            int loanfeests=0;
            if(Double.parseDouble(amm.getAbono().toString()) == Double.parseDouble(amm.getAbonopag().toString())){
               loanfeests=3;
            }else if(Double.parseDouble(amm.getAbono().toString())>Double.parseDouble(amm.getAbonopag().toString())
                    && amm.getTodopag()==false){
               loanfeests=1;
            }else if(!list.get(14).toString().equals("C")){
                loanfeests=2;
            }
            
            Loan_Fee_Status loanf=em.find(Loan_Fee_Status.class,loanfeests);
            
            Double abonoT=Double.parseDouble(amm.getAbono().toString())+iovencido+imvencido;
            Date d=amm.getVence();
            loanFee = new LoanFee(
                    Double.parseDouble(aux.getSaldo().toString()),//Saldo o balance del prestamo principal
                    amm.getAmortizacionesPK().getIdorigenp()+amm.getAmortizacionesPK().getIdproducto()+amm.getAmortizacionesPK().getIdauxiliar()+amm.getAmortizacionesPK().getIdamortizacion(),
                    Double.parseDouble(amm.getAbono().toString()),
                    d,
                    iovencido,
                    imvencido,
                    loanf.getId(),
                    Double.parseDouble(amm.getAbono().toString()),
                    abonoT);
        } catch (Exception e) {
            System.out.println("Error en LoanFee:"+e.getMessage());
        }

        return loanFee;      
     }
    
    //Obetner cuota vencida
    public FeesDueData RSFeesDueData(int o, int p, int a) {
        EntityManager em = emf.createEntityManager();
        String sai_auxiliar = "SELECT * FROM sai_auxiliar(" + o + "," + p + "," + a + ",(SELECT date(fechatrabajo) FROM origenes limit 1))";
        FeesDueData FeesDueDataRS = null;
        try {
            Query RsSai = em.createNativeQuery(sai_auxiliar);
            String sai = RsSai.getSingleResult().toString();
            String[] parts = sai.split("\\|");
            List list = Arrays.asList(parts);

            Double iovencido = Double.parseDouble(list.get(6).toString()) + Double.parseDouble(list.get(17).toString());
            Double imvencido = Double.parseDouble(list.get(15).toString()) + Double.parseDouble(list.get(18).toString());
            Double montovencido = Double.parseDouble(list.get(4).toString());
            Double mnttotalcv = iovencido + imvencido + montovencido;
            System.out.println("Iovencido:" + iovencido + ", imvencido:" + imvencido + ",montovencido:" + montovencido + "Total vencido:" + mnttotalcv);

            FeesDueDataRS = new FeesDueData(
                    iovencido,
                    Double.parseDouble(list.get(6).toString()),
                    imvencido,
                    montovencido,
                    mnttotalcv);
            System.out.println("FeesDueData:"+FeesDueDataRS);
        } catch (Exception e) {
            System.out.println("Error en FeesDueData:" + e.getMessage());
            cerrar();
        }
        System.out.println("FeesDueData:" + FeesDueDataRS);
        return FeesDueDataRS;
    }

    //Metodo para devolver abonos vencidos
    public int RSFeesDue(int o, int p, int a) {
        EntityManager em = emf.createEntityManager();
        String sai_auxiliar = "SELECT * FROM sai_auxiliar(" + o + "," + p + "," + a + ",(SELECT date(fechatrabajo) FROM origenes limit 1))";
        int abonosVencidos = 0;
        try {
            Query RsSai = em.createNativeQuery(sai_auxiliar);
            String sai = RsSai.getSingleResult().toString();
            String[] parts = sai.split("\\|");
            List list = Arrays.asList(parts);
            abonosVencidos = Integer.parseInt(list.get(3).toString());
            System.out.println("Abonos Vencidos:" + abonosVencidos);
        } catch (Exception e) {
            System.out.println("Error en FeesDueData:" + e.getMessage());
            cerrar();
        }
        return abonosVencidos;
    }
    
    //Devuelve LoanFee para apoyo en GetLoanPrincipal para obtener la proxima amortizacion
    public LoanFee RSLoanFee(int o, int p, int a) {
        EntityManager em = emf.createEntityManager();
        LoanFee loanFee = null;

        try {
            AuxiliaresPK pk = new AuxiliaresPK(o, p, a);
            Auxiliares aux = em.find(Auxiliares.class, pk);
            //Obtengo informacion con el sai_auxiliar hasta la fecha actual, si hay dudas checar el catalogo o atributos que devuelve la funcion
            String sai_auxiliar = "SELECT * FROM sai_auxiliar(" + o + "," + p + "," + a + ",(SELECT date(fechatrabajo) FROM origenes limit 1))";
            Query RsSai = em.createNativeQuery(sai_auxiliar);
            String sai = RsSai.getSingleResult().toString();
            String[] parts = sai.split("\\|");
            List list = Arrays.asList(parts);
            //Obtengo la amortizacion que se vence
            String consultaA="SELECT * FROM amortizaciones WHERE idorigenp="+o
                        +" AND idproducto="+p
                        +" AND idauxiliar="+a
                        +" AND vence='"+list.get(8)+"'";//en la pocision 8 esta la fecha de vencimiento
            Query queryA=em.createNativeQuery(consultaA,Amortizaciones.class);
            Amortizaciones amm=(Amortizaciones)queryA.getSingleResult();
            Double iovencido = Double.parseDouble(list.get(6).toString()) + Double.parseDouble(list.get(17).toString());
            Double imvencido = Double.parseDouble(list.get(15).toString()) + Double.parseDouble(list.get(18).toString());
            Double montovencido = Double.parseDouble(list.get(4).toString());
            
            int loanfeests=0;
            if(Double.parseDouble(amm.getAbono().toString()) == Double.parseDouble(amm.getAbonopag().toString())){
               loanfeests=3;
            }else if(Double.parseDouble(amm.getAbono().toString())>Double.parseDouble(amm.getAbonopag().toString())
                    && amm.getTodopag()==false){
               loanfeests=1;
            }else if(!list.get(14).toString().equals("C")){
                loanfeests=2;
            }
            
            Loan_Fee_Status loanf=em.find(Loan_Fee_Status.class,loanfeests);
            
            Double abonoT=Double.parseDouble(amm.getAbono().toString())+iovencido+imvencido;
            loanFee = new LoanFee(
                    Double.parseDouble(aux.getSaldo().toString()),//Saldo o balance del prestamo principal
                    amm.getAmortizacionesPK().getIdorigenp()+amm.getAmortizacionesPK().getIdproducto()+amm.getAmortizacionesPK().getIdauxiliar()+amm.getAmortizacionesPK().getIdamortizacion(),
                    Double.parseDouble(amm.getAbono().toString()),
                    amm.getVence(),
                    iovencido,
                    imvencido,
                    loanf.getId(),
                    Double.parseDouble(amm.getAbono().toString()),
                    abonoT);
        } catch (Exception e) {
            System.out.println("Error en LoanFee:"+e.getMessage());
        }

        return loanFee;
    }

    //Metodo para devolver dias vencidos
    public int RSOverdueDays(int o, int p, int a) {
        EntityManager em = emf.createEntityManager();
        String sai_auxiliar = "SELECT * FROM sai_auxiliar(" + o + "," + p + "," + a + ",(SELECT date(fechatrabajo) FROM origenes limit 1))";
        int diasVencidos = 0;
        try {
            Query RsSai = em.createNativeQuery(sai_auxiliar);
            String sai = RsSai.getSingleResult().toString();
            String[] parts = sai.split("\\|");
            List list = Arrays.asList(parts);
            diasVencidos = Integer.parseInt(list.get(3).toString());
        } catch (Exception e) {
            System.out.println("Error en FeesDueData:" + e.getMessage());
            cerrar();
        }
        return diasVencidos;
    }
    
    public static Date stringTodate(String fecha) {
        Date date = null;
        try {
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            date = formato.parse(fecha);
        } catch (ParseException ex) {
            System.out.println("Error al convertir fecha:" + ex.getMessage());
        }
        System.out.println("date:" + date);
        return date;
    }
    
      public String dateToString(Date cadena) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String cadenaStr = sdf.format(cadena);
        return cadenaStr;
    }

    /**
     * *********************************Cerrando conexiones
     * ***********************************
     */
    public void cerrar() {
        emf.close();
    }

}
