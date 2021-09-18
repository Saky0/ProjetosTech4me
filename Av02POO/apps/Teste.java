package apps;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.text.DateFormatter;

public class Teste {
    public static void main(String[] args) {
        
        /* List<Integer> a = new ArrayList<>();

        a.add(1);
        a.add(5);
        a.add(10);
        a.add(20);
        a.add(7);

        Double media = 0.0;

        for (Integer integer : a) {
            media += integer;
        }

        media = media / a.size();

        System.out.println(media); */

        Scanner in = new Scanner(System.in);
        LocalDate date; // Cria a variável de data
        
        // Formato desejado
        String dataDigitadaPeloUsuario = in.nextLine();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        date = LocalDate.parse(dataDigitadaPeloUsuario, formatter);

        System.out.println(date.format(formatter));


    }
}
