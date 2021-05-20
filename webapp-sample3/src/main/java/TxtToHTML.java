
import com.fenoreste.rest.Util.AbstractFacade;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.xhtmlrenderer.pdf.ITextRenderer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Elliot
 */
public class TxtToHTML {

    public static void main(String[] args) throws FileNotFoundException {
        try{
        File f=new File("C:\\Users\\Elliot\\estado_cuenta.txt");
        if(f.exists()){
            System.out.println("existe");
        FileOutputStream fs=new FileOutputStream("C:\\Users\\Elliot\\estado_cuentaNew.html");
        }}
    catch(Exception e){
            System.out.println("Error");
            }
    }
    /*Creando estados de cuenta*/
    public static String ruta() {
        String home = System.getProperty("user.home");
        String separador = System.getProperty("file.separator");
        return home + separador + "Banca" + separador;
    }

    public String runFileCreate(String opa, String initDate, String finalDate, String nombreTxt, String nombreHTML) {
        System.out.println("Run file");
        File f1 = new File(ruta() + nombreTxt);
        FileOutputStream fs = null;
        try {
            if (f1.exists()) {
                System.out.println("Existe");
                
                System.out.println("Se elimino");
                System.out.println("creando");
                crearLllenarTxt(opa, initDate, finalDate, nombreTxt);
                System.out.println("creado");
                File f2 = new File(ruta() + nombreTxt);
                if (f2.exists()) {
                    System.out.println("Existia y se elimino el html");
                    f2.delete();
                    System.out.println("Se crea el HTML");
                    fs = new FileOutputStream(ruta() + nombreHTML);
                    System.out.println("Se llena el html");
                    llenarHTML(obtenerTxt(nombreTxt), fs);
                    if (crearPDF(ruta(), nombreHTML)) {
                        f1.delete();
                        f2.delete();
                    }
                } else {
                    crearLllenarTxt(opa, initDate, finalDate, finalDate);
                    fs = new FileOutputStream(ruta() + nombreHTML);
                    System.out.println("Se llena el html en el else");
                    llenarHTML(obtenerTxt(nombreTxt), fs);
                    if (crearPDF(ruta(), nombreHTML)) {
                        f1.delete();
                        f2.delete();
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        }
        return nombreTxt.replace(".txt", "");
    }

    public void crearLllenarTxt(String opa, String initialDate, String finalDate, String fichero) {
        EntityManagerFactory emf = AbstractFacade.conexion();
        try {
            String o = opa.substring(0, 6);
            String p = opa.substring(6, 11);
            String a = opa.substring(11, 19);
            EntityManager em = emf.createEntityManager();
            String ruta = ruta() + fichero;
            /*String contenido;
            String consulta ="SELECT sai_estado_cuenta_ahorros("+o+","+p+","+a+",'"+initialDate+"','"+finalDate+"')";
            System.out.println("Consulta Statements:"+consulta);
            Query query = em.createNativeQuery(consulta);
            contenido = String.valueOf(query.getSingleResult());*/
            String contenido = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \n"
                    + "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n"
                    + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n"
                    + "  <head>\n"
                    + "    <title>estado_cuenta_ahorro</title>\n"
                    + "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n"
                    + "    <style type=\"text/css\">\n"
                    + "      .contenedor {\n"
                    + "          height: 25cm;\n"
                    + "          width: 19cm;\n"
                    + "          font-family: Arial, Helvetica, sans-serif;\n"
                    + "      }\n"
                    + "      .encabezado {\n"
                    + "          font-size: 14px;\n"
                    + "          text-align: center;\n"
                    + "          font-weight: bold;\n"
                    + "      }\n"
                    + "      .texto_sub {\n"
                    + "          text-align: center;\n"
                    + "          font-size: 8pt;\n"
                    + "      }\n"
                    + "      .fondo_celda {\n"
                    + "          background-color: #CCC;\n"
                    + "      }\n"
                    + "      .texto_der {\n"
                    + "          text-align: right;\n"
                    + "      }\n"
                    + "      .texto_top {\n"
                    + "          vertical-align: top;\n"
                    + "      }\n"
                    + "      .formato_texto {\n"
                    + "          font-size: 10pt;\n"
                    + "      }\n"
                    + "      .formato_texto_sub {\n"
                    + "          font-size: 11pt;\n"
                    + "          text-align: center;\n"
                    + "      }\n"
                    + "      .borde_total {\n"
                    + "          border: 2px solid #000;\n"
                    + "      }\n"
                    + "      .borde_bajo,  .borde_arriba_abajo, .borde_c, .borde_resumen {\n"
                    + "          border-bottom-width: 1px;\n"
                    + "          border-bottom-style: solid;\n"
                    + "          border-bottom-color: #000;\n"
                    + "      }      \n"
                    + "      .borde_arriba, .borde_arriba_abajo, .borde_c, .borde_resumen {\n"
                    + "          border-top-width: 1px;\n"
                    + "          border-top-style: solid;\n"
                    + "          border-top-color: #000;\n"
                    + "      }\n"
                    + "      .borde_der_2  {\n"
                    + "          border-right-width: 2px;\n"
                    + "          border-right-style: solid;\n"
                    + "          border-right-color: #000;\n"
                    + "      }\n"
                    + "      .borde_izq, .borde_c, .borde_resumen {\n"
                    + "          border-left: 1px solid #000;\n"
                    + "      }\n"
                    + "      .borde_der, .borde_resumen{\n"
                    + "          border-right: 1px solid #000;\n"
                    + "      }\n"
                    + "      .borde_bajo_2 {\n"
                    + "          border-bottom-width: 2px;\n"
                    + "          border-bottom-style: solid;\n"
                    + "          border-bottom-color: #000;\n"
                    + "      }      \n"
                    + "      .borde {\n"
                    + "          border-top-width: 2px;\n"
                    + "          border-right-width: 2px;\n"
                    + "          border-bottom-width: 2px;\n"
                    + "          border-left-width: 2px;\n"
                    + "          border-top-style: none;\n"
                    + "          border-right-style: none;\n"
                    + "          border-bottom-style: solid;\n"
                    + "          border-left-style: none;\n"
                    + "          border-top-color: #000;\n"
                    + "          border-right-color: #000;\n"
                    + "          border-bottom-color: #000;\n"
                    + "          border-left-color: #000;\n"
                    + "      }\n"
                    + "      .sub_encabe_tabla {\n"
                    + "          text-align: center;\n"
                    + "          font-weight: bold;\n"
                    + "          text-align: center;\n"
                    + "          font-size: 8pt;\n"
                    + "          border-bottom-width: 2px;\n"
                    + "          border-bottom-style: solid;\n"
                    + "          border-bottom-color: #000;\n"
                    + "      }  \n"
                    + "    </style>\n"
                    + "  </head>\n"
                    + "  <body>";
            File file = new File(ruta);
            // Si el archivo no existe es creado
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write(contenido);
            bw.close();
            /* em.clear();
            em.close();*/
        } catch (Exception e) {
            emf.close();
            e.printStackTrace();
            System.out.println("Error:" + e.getMessage());
        }
        emf.close();
    }

    public static File obtenerTxt(String fichero) {
        String sf = ruta() + fichero;
        File f = new File(sf);
        if (f.exists()) {
            return f;
        } else {
            System.out.println("El fichero no existe: " + sf);
            return null;
        }
    }

    public static void llenarHTML(File file, FileOutputStream fs) {
        OutputStreamWriter out = new OutputStreamWriter(fs);
        File f=obtenerTxt("C:\\Users\\Elliot\\estado_cuent.txt");
        try {
            if(f.exists()){
                System.out.println("si");
            }
            FileReader file1R=new FileReader(f);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String linea;
            while ((linea = br.readLine()) != null) {
                System.out.println("linea:" + linea);
                if (linea.contains("/usr/local/saicoop/img_estado_cuenta_ahorros/")) {
                    System.out.println("si");
                    String cade = ruta();
                    System.out.println("Cade:" + cade.replace("\\", "/"));
                    linea = linea.replace("/usr/local/saicoop/img_estado_cuenta_ahorros/", cade.replace("\\", "/"));
                }
                if (linea.contains("&nbsp;")) {
                    linea = linea.replace("&nbsp;", "");
                }
                out.write(linea);
            }
            out.close();
        } catch (Exception e) {
            System.out.println("Excepcion leyendo txt" + ": " + e.getMessage());
        }
    }

    public boolean crearPDF(String ruta, String nombreHTML) {
        try {
            String ficheroHTML = ruta + nombreHTML;
            System.out.println("fichero" + ficheroHTML);
            String url = new File(ficheroHTML).toURI().toURL().toString();
            String ficheroPDF = ruta + nombreHTML.replace(".html", ".pdf");
            OutputStream os = new FileOutputStream(ficheroPDF);
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocument(url);
            renderer.layout();
            renderer.createPDF(os);
            os.close();
            return true;
        } catch (Exception e) {
            System.out.println("Error al crear PDF:" + e.getMessage());
            return false;
        }

    }
}
