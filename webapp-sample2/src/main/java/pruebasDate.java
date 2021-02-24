
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Elliot
 */
public class pruebasDate {
    public static void main(String[] args) throws ParseException {
        SimpleDateFormat formato=new SimpleDateFormat("dd/MM/yyyy");
        Date fecha=formato.parse("20/10/2020");
        System.out.println("Fecha:"+fecha);
    }
}
