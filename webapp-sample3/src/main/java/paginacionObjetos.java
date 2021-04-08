
import static java.lang.Integer.min;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toMap;
import java.util.stream.IntStream;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Elliot
 */
public class paginacionObjetos {

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        Map<Integer, String> map = IntStream
                .range(1, list.size())
                .boxed()
                .collect(groupingBy(
                        i -> i / 4, //no longer i-1 because we start with 0
                        mapping(i -> list.get((int) i).toString(), joining(","))
                ));
     
        System.out.println("Map:"+map.get(1));
    }
}
 
               