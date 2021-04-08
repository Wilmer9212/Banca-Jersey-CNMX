
import static java.time.Instant.ofEpochMilli;
import java.time.LocalDate;
import java.time.ZoneId;
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
public class pruebaDate {
         public static void main(String[] args) {
        long milliseconds = 1622696400000L;
        LocalDate localDate = ofEpochMilli(milliseconds).atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate utcDate = ofEpochMilli(milliseconds).atZone(ZoneId.of("UTC")).toLocalDate();
        System.out.println("milliseconds: " + milliseconds);
        System.out.println("Local Date: " + localDate);
        System.out.println("UTC Date: " + utcDate);
        
        
            ZoneId defaultZoneId = ZoneId.systemDefault();
         long m=134678800009L;//loan.getDueDate().getTime();
             LocalDate localDates = ofEpochMilli(m).atZone(ZoneId.systemDefault()).toLocalDate();
             Date date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
    }
}
