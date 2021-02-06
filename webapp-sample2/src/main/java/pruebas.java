
import java.util.Formatter;

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
        String.format("%010d", 4);
        Formatter fmt = new Formatter();
        int numero=2901;
        Formatter h=fmt.format("%08d",numero);
        System.out.println("h:"+h);
        for(int x=0;x<5;x++){
            System.out.println("x:"+x);
        }
    }
    
}




