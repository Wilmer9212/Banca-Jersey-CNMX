/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fenoreste.rest.WsTDD;

import com.fenoreste.rest.Util.AbstractFacade;
import com.fenoreste.rest.entidades.Tablas;
import com.fenoreste.rest.entidades.TablasPK;
import com.syc.ws.endpoint.siscoop.impl.SiscoopAlternativeEndpoint;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceException;

/**
 *
 * @author Elliot
 */
public class SiscoopTDD {

    EntityManagerFactory emf=AbstractFacade.conexion();
    
    
    // REALIZA LA AUTENTIFICACIÓN
    public SiscoopTDD() {
        EntityManager em=emf.createEntityManager();
        try{
        java.net.Authenticator.setDefault(new java.net.Authenticator() {
            // Pruebas: USUARIO: snicolas - PASSWORD: wsn4yc8ja$s
            // Produccion: USUARIO: ws_snicolas - PASSWORD: wu8K2SyJbjYc9rw
            @Override
            
            protected java.net.PasswordAuthentication getPasswordAuthentication() {
                // Credenciales SYC
                TablasPK tablasPK = new TablasPK("siscoop_banca_movil", "wsdl_parametros");
                Tablas tablasDTO = em.find(Tablas.class,tablasPK);
                return new java.net.PasswordAuthentication(tablasDTO.getDato1(), tablasDTO.getDato2().toCharArray());
            }
           
        });
        }catch(Exception e){
            em.close();
            em.close();
            System.out.println("Error al autentitar:"+e.getMessage());
        }
    }
    
    // REALIZA UN PING A LA URL DEL WSDL
    private boolean pingURL(URL url, String tiempo) {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(Integer.parseInt(tiempo));
            connection.setReadTimeout(Integer.parseInt(tiempo));
            int codigo = connection.getResponseCode();

            if (codigo == 200) {
                return true;
            }
        } catch (IOException ex) {
            System.out.println("Error al conectarse a SYC: " + ex.getMessage());
        }
        return false;
    }

    // GENERA EL PUERTO PARA SYC
    public SiscoopAlternativeEndpoint siscoop() {
        EntityManager em=emf.createEntityManager();
        try {
            // Parametros SYC
            TablasPK tablasPK = new TablasPK("siscoop_banca_movil", "wsdl");
            Tablas tablasDTO = em.find(Tablas.class, tablasPK);
            String wsdlLocation = "http://" + tablasDTO.getDato1() + ":" + tablasDTO.getDato3() + "/syc/webservice/" + tablasDTO.getDato2() + "?wsdl";
            //"http://200.15.1.143:8080/syc/webservice/siscoopAlternativeService/?wsld"
            QName QNAME = new QName("http://impl.siscoop.endpoint.ws.syc.com/", "SiscoopAlternativeEndpointImplService");
            URL url = new URL(wsdlLocation);
            if (pingURL(url, tablasDTO.getDato4())) {
                Service service = Service.create(url, QNAME);
                SiscoopAlternativeEndpoint port = service.getPort(SiscoopAlternativeEndpoint.class);
                return port;
             }
            em.close();
            emf.close();
           }catch (MalformedURLException | WebServiceException ex) {
               em.close();
               emf.close();
               System.out.println(ex.getMessage());
           }
        return null;
    }

    // PUERTO
    public SiscoopAlternativeEndpoint getSiscoop() {
        return siscoop();
    }

}

