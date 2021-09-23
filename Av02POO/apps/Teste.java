package apps;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import classes.*;


public class Teste {
    public static void main(String[] args) {
        
        /*
            Criei este app apenas para testar algumas classes e criar exemplos pra ajudar os outros alunos na 
            Prova :3, com pequenos exemplos práticos e testando alguma coisa para ajudar
        */

        /* 
        System.out.println(media); */

        /*Scanner in = new Scanner(System.in);
        LocalDate date; // Cria a variável de data
        
        // Recebe uma String que deve ser no formato decidido dd/MM/yyyy
        String dataDigitadaPeloUsuario = in.nextLine();

        // Formato desejado
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Passa para o LocalDate a String digitada pelo usuário + o formato que agt criou em cima
        date = LocalDate.parse(dataDigitadaPeloUsuario, formatter);

        System.out.println(date.format(formatter));

        in.close();*/
        /*DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String dataIncial = LocalDate.now().format(formatter);

        LocalDate data = LocalDate.parse(dataIncial, formatter);
        LocalDate data2 = LocalDate.parse("14/09/2021", formatter);
        LocalDate data3 = LocalDate.parse("16/09/2021", formatter);
        LocalDate data4 = LocalDate.parse("17/09/2021", formatter);
        LocalDate data5 = LocalDate.parse("20/09/2021", formatter);
        LocalDate data6 = LocalDate.parse("15/09/2021", formatter);

        List<Daata> lista = new ArrayList<>();
        lista.add(new Daata(data3, "Bala 3"));
        lista.add(new Daata(data4, "Bala 4"));
        lista.add(new Daata(data5, "Bala 5"));
        lista.add(new Daata(data6, "Bala 6"));
        lista.add(new Daata(data6, "Bala 6"));
        lista.add(new Daata(data6, "Bala 6"));

        System.out.printf("\nData Incial: %s", data2.format(formatter).toString());
        System.out.printf("\nData Final: %s\n\n", data.format(formatter).toString());

        lista.stream()
        /*
            Caso não seja depois da data inicial ou antes de data final
            Cria mais duas situações para caso seja igual a uma das datas
        */
        /*.filter(v -> (v.getDataVenda().isBefore(data) // Antes da data Final
            && v.getDataVenda().isAfter(data2) // Depois da Data Incial
            || v.getDataVenda().isEqual(data) // Igual a Data Final
            || v.getDataVenda().isEqual(data2))) // Igual a Data Incial
        .forEach(System.out::println);*/

        /*
            Primeiro, criamos um formato da nossa data, aqui no Brasil é o comum dd/mm/yyyy
            Usa-se a Classe DateTimeFormatter 
            Esse formato vc pode criá-lo no inicio do programa, pois deve usar nele inteiro
        */
        Scanner in2 = new Scanner(System.in);
        String dataDigitadaPeloUsuario2 = in2.nextLine();
        String dataDigitadaPeloUsuario3 = in2.nextLine();

        if( !dataDigitadaPeloUsuario2.matches("[0-9A-Za-z\\.]*")) {
            System.out.println("DIGITA DIREITO");
        }



        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        /*
            Para atribuir uma data digitada pelo usuário, devemos:
                1º Cria uma String com a entrada dele, e pedir q digite no formato dd/mm/yyyy
                    -> AVISO: Você deve fazer aquele sistema para evitar Exceptions, verificando se a entrada do usuário está no
                        Padrào Correto

                2º Criamos nosso Objeto LocalDate e convertemos a entrada no Usuário para o formato criado
        */
        Scanner in = new Scanner(System.in);
        String dataDigitadaPeloUsuario = in.nextLine();

        // O método parse() recebe uma String e converte para o padrão especificado a seguir
        LocalDate dataNova = LocalDate.parse(dataDigitadaPeloUsuario, formatter /* Esse é o padrão criado */);
        
        /*
            Para passar a data atual, fazemos o mesmo, porém sem o uso da entrada do usuário
            Apenas usando o método now()
        */

        String dataAtual = LocalDate.now() // Recebe a data atual no formato padrão de datas
            .format(formatter); // Converte a data recebida para o nosso formato

        // O método parse() recebe uma String e converte para o padrão especificado a seguir
        LocalDate dataNova2 = LocalDate.parse(dataAtual, formatter /* Esse é o padrão criado */);

       
    }
}
