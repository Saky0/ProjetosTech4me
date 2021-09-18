package apps;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.List;

import classes.*;
import exceptions.DataInvalida;


public class ProgramaLoja {

    public static void main (String[] args) throws InterruptedException, IOException{
        
        /* 
            Constantes usadas para montar as "tabelas"
                System.out.format(formatoDoSyouFormatTitulo, "Titulo", "qtd", "Descricao");
                System.out.println(dividirTela);
                System.out.format("%32s%10d%16s", "1adsadasasdsadasdasdsadasdd", 22, "123e123s");
                System.out.println();
                System.out.format("%32s%10d%16s", "asdsadasd", 22, "123e123s");
                System.out.println();
                System.out.format("%32s%10d%16s", "asdsadasd", 22, "123e123s");
                System.out.println();
                System.out.println(dividirTela);
        */
        final String dividirTela = "\n------------------------------------------------------------------";
        final String formatoDoSyouFormatTitulo = "%32s%10s%16s";
        final String formatoDoSysouFormatProdutos = "";

        // 
        List<Produto> produtosCadastrados = new ArrayList<>();
        List<Venda> vendasRealizadas = new ArrayList<>();
        
        //

        int opcao = 5;

        Scanner in = new Scanner(System.in);

        do {

            System.out.println("======================");
            System.out.println("==== Menu da Loja ====");
            System.out.println("======================");
            System.out.println("1 - Area de Produtos");
            System.out.println("2 - Relatorios");
            System.out.println("3 - Registrar Venda");
            System.out.println("0 - Sair");
            System.out.print("\nOpcao: ");

            try { 
                
                opcao = in.nextInt();
                in.nextLine();
                
            } catch(InputMismatchException ex) {
                System.out.println("Digite Somente Números !!");
                TimeUnit.SECONDS.sleep(1);
                in.nextLine();
                opcao = -1; // Evita que valores anteriores atrapalhem o funcionamento
            } cls();
            

            /*
                Produtos - Opção 1:
                Cadastrar um Novo Produto;
                Vizualizar os Existentes 
                    - Buscando pelo ID ou listando todos
            */
            if(opcao == 1) {

                System.out.println("===========================");
                System.out.println("===== Area de Produtos =====");
                System.out.println("===========================");
                System.out.println("1 - Listar Produtos / Buscar por Id");
                System.out.println("2 - Incluir Novo Produto");
                System.out.println("0 - Sair");
                System.out.print("\nOpcao: ");
                
                
                opcao = verificarEntradaParaMenu(in.nextLine());
                cls();
                

                // READ
                // Listar um Produto ou Procurar algum específico por Id
                if(opcao == 1) {

                    /*
                    Entradas:
                        -> ENTER: Exibe todos os Produtos Cadastrados
                        -> Int: Procura um produto com o código indicado
                        

                    */
                }

                // CREATE
                // Inseri um novo produto no sistema
                else if(opcao == 2) {

                    

                    System.out.println("=============================");
                    System.out.println("===== Cadastrar Produto =====");
                    System.out.println("=============================");

                    System.out.println("** Caso queira voltar, digite qualquer caractere alfabético ** \n");

                    System.out.println("Digite o Id do Produto:");

                    /* 
                        Estrutura de If para tratar com caso seja digitado um valor não númerico para sair do Cadastrar Produto
                        Se opção for = 0, o usuário digitou algum caractere alfabético e vai retornar
                        Se opção for = [0-9], segue o Cadastro
                    */
                    int codigo = verificarEntradaParaMenu(in.nextLine());

                    if(codigo == 0) { // Termina a Função e retorna para o Menu
                        voltarMenu();
                    }

                    Produto produto = new Produto();
                    // Caso passe daqui, continua o Cadastro
                    produto.setCódigo(codigo);

                    System.out.println("\nDigite o Nome do Produto: ");
                    produto.setNome(in.nextLine());

                    System.out.println("\nDigite o Valor do Produto: ");
                    boolean verificador = false;
                    do {
                        try{
                            
                            produto.setValor(in.nextDouble());

                            verificador = true;

                        } catch(InputMismatchException ex) {
                            System.out.println("Digite Apenas numeros e ponto !!\nValor:");
                        }
                    } while(!verificador);
                    verificador = false;


                    System.out.println("\nDigite a Quantidade de estoque do Produto: ");
                    do {
                        try{
                            produto.setQtdEstoque(in.nextInt());

                            verificador = true;

                        } catch(InputMismatchException ex) {
                            System.out.println("Digite Apenas numeros !!\nQuantidade: ");
                        }
                    } while(!verificador);


                    produtosCadastrados.add(produto);
                    System.out.println("Produto Cadastrado Com Sucesso!");
                    TimeUnit.SECONDS.sleep(1);
                    
                }
                
                // RETURN
                // Retorna ao Menu Principal
                else if(opcao == 0) {
                    voltarMenu();
                }

                voltarMenu(); // Após sair da opção 1 ou 2, ja executa o método

            }

            /*
                Vendas - Opção 2:
                Inserir um novo Registro de Venda no Sistema
            */
            else if(opcao == 2) {

            }
            /*
                Relatório - Opção 3:
                Gera relatório de:
                    - Produtos cadastrados
                    - Vendas (Detalhadas)
            */
            else if(opcao == 3) {

            }
            /*
                Erro de Opção - Opção != 0 e opcão != -1:
            */
            else if(opcao != 0 && opcao != -1) { // Verifição Extra para evitar aparecer duas mensagens de error na tela
                System.out.println("Opção Inválida !!");
                TimeUnit.SECONDS.sleep(1);
                voltarMenu();
            }

        } while(opcao != 0);

        /*
            Encerrar Programa - Opção 0:
        */
        System.out.println("\nFim do Programa!");
        TimeUnit.MILLISECONDS.sleep(1500);
        in.close();
    } // End Main

    // Funcão para dar um Clear no Console
    private static void cls() throws IOException, InterruptedException {
        
        // Limpa toda a tela, deixando novamente apenas o menu
        if (System.getProperty("os.name").contains("Windows"))
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        else
            System.out.print("\033[H\033[2J");
        
        System.out.flush();  
    }



     /* 
        Recebe uam entrada do usuário e verifica se contém somente caracteres numéricos
         É Usado uma string para obter a entrada para facilitar a verificação 
         Caso o valor seja um numero, retorna o próprio valor 
         Caso seja um caractere alphanumérico ou alphabétitco, Retorna zero como forma de sair do Menu
         Não possui throw para exceções, pois sempre vai retornar valores inteiros, seja 0 para não numérico ou o proprio numero
     */
    private static int verificarEntradaParaMenu(String valor) {
        if(valor.matches("[0-9]*")) {
            return Integer.parseInt(valor);

        } else{
            return 0;
        }
    }

    // Função de Retornar ao menu chamando o método cls() para limpar o console
    private static void voltarMenu()  throws InterruptedException, IOException{
        System.out.println("Retornando ao Menu...");
        TimeUnit.SECONDS.sleep(1);
        cls();
    }

}