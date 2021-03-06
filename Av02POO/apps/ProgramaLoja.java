package apps;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import static java.time.format.DateTimeFormatter.ofPattern;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.time.LocalDate.now;


import classes.*;
import exceptions.*;


public class ProgramaLoja {
    
    
    //#region Constantes usadas para montar as "tabelas" ou outros atributos dependentes de constantes
    final static String dividirTelaRelatorioVendas = "\n-------------------------------------------------------------------------------------------------------------------";
    final static String dividirTelaProdutos = "\n-----------------------------------------------------------------------------------------------";
    final static String formatoDoSyouFormatTitulo = "%10s %40s %17s %17s";
    final static String formatoDoSysouFormatProdutos = "%10d %40s %17.2f %17d";
    final static String formatoRodapeProdutos = "%25s %25s %25s";
    final static DateTimeFormatter formatter = ofPattern("dd/MM/yyyy");
    final static String padraoData = "\\d{2}/\\d{2}/\\d{4}";
    final static String formatoTituloRelatorioVenda = "%15s %18s %40s %12s %21s";
    final static String formatoCorpoRelatorioVenda = "%15s %18.2f %40s %12d %21.2f";
    //#endregion

    public static void main (String[] args) throws InterruptedException, IOException, QuantidadeNegativaOuZeroException{
        
        // 
        List<Produto> produtosCadastrados = new ArrayList<>();
        List<Venda> vendasRealizadas = new ArrayList<>();

     // TESTES
        produtosCadastrados.add(new Produto(1, "Pao", 1.0, 5));
        produtosCadastrados.add(new Produto(2, "Manteiga", 4.0, 5));

        vendasRealizadas.add(new Venda(LocalDate.parse(now().format(formatter), formatter), produtosCadastrados.get(0), 2));
        vendasRealizadas.add(new Venda(LocalDate.parse("15/09/2020", formatter), produtosCadastrados.get(1), 2));

        for (Venda venda : vendasRealizadas) {
            venda.finalizarVenda();
        }
 
        int opcao = 5;

        Scanner in = new Scanner(System.in);

        do {

            System.out.println("======================");
            System.out.println("==== Menu da Loja ====");
            System.out.println("======================");
            System.out.println("1 - Area de Produtos");
            System.out.println("2 - Relatorio de Vendas");
            System.out.println("3 - Registrar Venda");
            System.out.println("0 - Sair");
            System.out.print("\nOpcao: ");

            try { 
                
                opcao = in.nextInt();
                in.nextLine();
                
            } catch(InputMismatchException ex) {
                System.out.println("Digite Somente N??meros !!");
                SECONDS.sleep(1);
                in.nextLine();
                opcao = -1; // Evita que valores anteriores atrapalhem o funcionamento
            } cls();
            

            /*
                Produtos - Op????o 1:
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
                // Listar um Produto ou Procurar algum espec??fico por Id
                if(opcao == 1) {

                    if(produtosCadastrados.size() == 0) {
                        System.out.println("N??o existem produtos cadastrados para serem listados !!");
                        SECONDS.sleep(1);
                        voltarMenu();
                        continue;
                    }
                    // Sempre antes  de listar quaisquer produto, organiza a lista por ordem crescente dos Id dos produtos
                    produtosCadastrados.sort(new CompararProdutosPorId());
                    /*
                        Entradas:
                        -> ENTER ou Caractere Alfab??tico: Exibe todos os Produtos Cadastrados
                            Neste caso, digitar um caractere n??o num??rico prossegue com a mesma fun????o do ENTER:
                            Listar todos os produtos cadastrados
                        -> Int: Procura um produto com o c??digo indicado
                    */
                    System.out.print("\n-> Digite o codigo do produto para fazer uma busca do produto");
                    System.out.println("\n-> Aperte ENTER ou digite alguma letras para Listar Todos os produtos + relatorio");
                    System.out.print("\nCodigo: ");
                    String verificacaoEntrada = in.nextLine();


                    if(verificacaoEntrada.equals("") || !verificacaoEntrada.matches("[0-9]*")) { // Caso a entrada seja Apenas ENTER, lista todos existentes

                        listarProdutosCadastrados(produtosCadastrados); // M??todo para listar todos os produtos cadastrados 

                    } else { // Segue caso tenha digitado algum c??digo v??lido


                        /*
                            Se chegou aqui, a entrada possui caracters num??ricos v??lidos para buscar o c??digo
                            Chama o m??todo de verifica????o
                            1?? Atribui para c??digo a entrada do usuario
                            2?? Busca com o m??todo buscarProdutoPorId() que vai retornar a posi????o na lista que o produto se 
                                encontrada, caso n??o encontre nada, gera uma exce????o de NullPointerException()
                        */
                        int codigoProduto = Integer.parseInt(verificacaoEntrada);

                        //
                        Produto produtoEncontrado = new Produto();

                        try{
                            produtoEncontrado = listarProdutoPorId(produtosCadastrados, codigoProduto);
                        } catch(NoSuchElementException ex) {
                            System.out.println("Nao foi encontrado nenhum produto com o Codigo: " + codigoProduto);
                            System.out.println("\nPressioner ENTER para sair");
                            in.nextLine();
                            voltarMenu();
                            continue;
                        }
                        // Mesmo formato de Exibi????o da Lista de Produtos, por??m adaptada para somente um Produto
                        System.out.println("\nProduto Encontrado: \n");
                        System.out.format(formatoDoSyouFormatTitulo, "Id", "Nome Produto", "Valor", "Estoque");
                        System.out.print(dividirTelaProdutos);

                        System.out.println();
                        System.out.format(formatoDoSysouFormatProdutos, produtoEncontrado.getCodigo(),
                            produtoEncontrado.getNome(), produtoEncontrado.getValor(), produtoEncontrado.getQtdEstoque() );
                        
                
                        System.out.print(dividirTelaProdutos);
                        /////
                    }


                    // Ap??s Listar, fica em espera at?? o usuario Pressionar ENTER
                    System.out.println("\n\nPressioner ENTER para sair");
                    in.nextLine();
                }

                // CREATE
                // Inseri um novo produto no sistema
                else if(opcao == 2) {

                    System.out.println("=============================");
                    System.out.println("===== Cadastrar Produto =====");
                    System.out.println("=============================");

                    System.out.println("** Caso queira voltar, digite qualquer caractere alfab??tico ** \n");

                    System.out.println("Digite o Id do Produto:");
                    Produto produto = new Produto();

                    /* */
                    int codigo = 0;
                    boolean verificador = false;
                    do{
                        try{
                            codigo = in.nextInt();
                            in.nextLine();

                            // Verifica se j?? existe algum produto com este c??digo, se sim, pede para digitar novamente
                            if(verificarCodigoRepetidoNoArray(produtosCadastrados, codigo)) {
                                System.out.println("Este codigo ja pertence a um produto, digite novamente !!");
                                System.out.print("\nCodigo: ");
                                continue;
                            }

                            produto.setCodigo(codigo);
                            verificador = true;

                        } catch(InputMismatchException ex) {
                            /*
                                Estrutura para verificar se o usu??rio digitou algum caractere alfab??tico para retornar ao 
                                Meno, caso sim, a fun????o abaixo deixa verificador como false 
                                E um If retorna-o para o menu
                            */
                            in.nextLine();
                            verificador = false;
                            break;

                        } catch(CodigoNegativoException ex) {
                            /* 
                                Estrutura para tratar a poss??vel exce????o do usu??rio digitar um c??digo que seja negativo
                            */  
                            System.out.println(ex.getMessage());
                            System.out.print("\nC??digo: ");
                        }

                    }while(!verificador);

                    if(!verificador) {
                        voltarMenu();
                        continue;
                    }
                    /* */

                    System.out.println("\nDigite o Nome do Produto: ");
                    produto.setNome(in.nextLine());

                    System.out.println("\nDigite o Valor do Produto: ");
                    verificador = false;
                    do {
                        try{
                            
                            produto.setValor(in.nextDouble());
                            in.nextLine();

                            verificador = true;

                        } catch(InputMismatchException ex) {
                            System.out.println("Digite Apenas numeros e ponto !!");
                            System.out.print("\nValor: ");
                            in.nextLine();
                        } catch(QuantidadeNegativaOuZeroException ex) {
                            System.out.println(ex.getMessage());
                            System.out.print("\nValor: ");
                            in.nextLine();
                        }

                    } while(!verificador);
                    verificador = false;


                    System.out.println("\nDigite a Quantidade de estoque do Produto: ");
                    do {
                        try{
                            produto.setQtdEstoque(in.nextInt());
                            in.nextLine();

                            verificador = true;

                        } catch(InputMismatchException ex) {
                            System.out.println("Digite Apenas numeros !!");
                            System.out.print("\nQuantidade: ");
                            in.nextLine();
                        } catch(QuantidadeNegativaOuZeroException ex) {
                            System.out.println(ex.getMessage());
                            System.out.print("\nQuantidade: ");
                            in.nextLine();
                        }

                    } while(!verificador);
                    produtosCadastrados.add(produto);
                    System.out.println("Produto Cadastrado Com Sucesso!");
                    SECONDS.sleep(1);
                    
                    
                }
                
                // RETURN
                // Retorna ao Menu Principal
                else if(opcao == -1) {
                    voltarMenu();
                    continue;
                }

                // Op????o Inv??lida
                else if(opcao != 0) {
                    System.out.println("Op????o Inv??lida !!");
                }

                voltarMenu(); // Ap??s sair da op????o 1 ou 2, ja executa o m??todo
                opcao = -1;

            }

            /*
                Relat??rio - Op????o 2:
                Gera relat??rio de:
                    - Produtos cadastrados
                    - Vendas (Detalhadas)
            */
            else if(opcao == 2) {

                if(vendasRealizadas.size() == 0) { // Retorna ao meno caso n??o haja nenhuma venda cadastrada ainda
                    System.out.println("N??o existe nenhuma venda no momento para gerar Relat??rios !!\n");
                    SECONDS.sleep(1);
                    voltarMenu();
                    continue;
                }

                System.out.println("=================================");
                System.out.println("====== Relat??rio de Vendas =====");
                System.out.println("=================================");
                System.out.println("Determine o per??odo que esta contido as vendas");
                System.out.print("\nData incial [dd/mm/yyyy] (Digite ENTER para inserir a Data de Hoje): ");

                LocalDate dataInicial = LocalDate.parse("2007-12-03");;
                boolean verificador = false;
                do {
                    try{

                        String data = in.nextLine(); // Para recolher o ENTER caso deseja a data atual
                        if(data.equals("")) {
                            String formatoPadrao = LocalDate.now().format(formatter);
                            dataInicial = LocalDate.parse(formatoPadrao, formatter);
                            verificador = true;
                            continue;
                        }
                        dataInicial = LocalDate.parse(data, formatter);

                        verificador = true;

                    } catch(DateTimeParseException ex)  {
                        System.out.println("\nDigite a data no padrao informado ou Pressione ENTER para colocar a data atual !!");
                        System.out.print("\nData Inicial [dd/mm/yyyy]: ");
                    }

                } while(!verificador);

                System.out.print("\nData Final [dd/mm/yyyy] (Digite ENTER para inserir a Data de Hoje): ");

                verificador = false;
                LocalDate dataFinal = LocalDate.parse("2007-12-03"); // Atribui????o inicial para evitar erros de compila????o
                do {
                    try{
                        String data = in.nextLine(); // Para recolher o ENTER caso deseja a data atual
                        if(data.equals("")) {
                            String formatoPadrao = LocalDate.now().format(formatter);
                            dataFinal = LocalDate.parse(formatoPadrao, formatter);
                            verificador = true;
                            continue;
                        }

                        // Verifica????o para impedir que a data final se antes da data inicial
                        if(dataInicial.isAfter(dataFinal)) {
                            System.out.println("A data Final nao pode ser antes da inicial");
                            System.out.print("\nData Final [dd/mm/yyyy]: ");
                            continue;
                        }

                        dataFinal = LocalDate.parse(data, formatter);

                        verificador = true;

                    } catch(DateTimeParseException ex)  {
                        System.out.println("\nDigite a data no padrao informado ou Pressione ENTER para colocar a data atual !!");
                        System.out.print("\nData Final [dd/mm/yyyy]: ");
                    }

                } while(!verificador);


                /*
                    Ap??s definir o intervalo das vendas (N??o pode exceder a data atual)
                    Chama o m??todo que constr??i a "tabela" com o relat??rio
                */
                gerarRelatorioVendas(vendasRealizadas, dataInicial, dataFinal);
                

                // End Gerar Relat??rios de Vendas
                System.out.println("\n\nPressioner ENTER para Sair");
                in.nextLine();
                voltarMenu();
                

            }
            
            /*
                Vendas - Op????o 3:
                Inserir um novo Registro de Venda no Sistema
            */
            else if(opcao == 3) {

                if(produtosCadastrados.size() == 0) {
                    System.out.println("N??o existem produtos cadastrados para vender!!");
                    voltarMenu();
                    opcao = -1;
                    continue;
                }

                System.out.println("==========================");
                System.out.println("===== Realizar Venda =====");
                System.out.println("==========================");

                System.out.println("** Caso queira voltar, digite quaisquer caractere alfab??tico ** \n");

                System.out.println("Digite o Id do Produto:");


                
                String verificacaoEntrada = ""; 
                boolean verificador = false;
                Venda novaVenda = new Venda();

                /*
                    Repeti????o para fazer a coleta se caso um c??digo v??lido, inv??lidou ou caractere alfab??tico seja
                    digitado pelo usu??rio
                */
                do{
                    try{
                        verificacaoEntrada = in.nextLine();
                        // Cuida de caso digitar algum caractere alfab??tico, retorna ao Menu
                        if(!verificacaoEntrada.matches("[0-9]*") || verificacaoEntrada.equals("")) {
                            break;
                        }

                        // Passa o valor digitado para "codigo"
                        int codigo = Integer.parseInt(verificacaoEntrada);

                        // Passo o codigo para a Stream e busca um produto com o c??digo fornecido
                        novaVenda.setProdutoVendido(produtosCadastrados.stream()
                        .filter(p -> p.getCodigo() == codigo)
                        .findFirst()
                        .get()); 
                        
                        verificador = true;
                    } catch(NoSuchElementException ex) {
                        System.out.println("\nNao foi encontrado nenhum produto com o Codigo fornecido");
                        System.out.println("Digite Novamente um c??digo v??lido");
                        System.out.print("\nCodigo: ");
                        
                    } 

                }while(!verificador);

                if(!verificador) { // Sa??da para o Menu caso digite caracteres alfab??ticos
                    voltarMenu();
                    continue;
                }

                // Prossegue o cadastro das vendas
                verificador = false;

                System.out.printf("Digite a quantidade de Venda do Produto \"%s\"", novaVenda.getProdutoVendido().getNome());
                System.out.print("\nQuantidade: ");
                do {
                    try { 
                
                        novaVenda.setQtdProdutoVendido(in.nextInt());
                        in.nextLine();

                        verificador = true;
                        
                    } catch(InputMismatchException ex) {
                        System.out.println("Digite Somente N??meros !!");
                        SECONDS.sleep(1);
                        in.nextLine();

                    } catch(QuantidadeNegativaOuZeroException ex) {
                        System.out.println(ex.getMessage());
                        System.out.print("\nQuantidade: ");
                        in.nextLine();
                    }

                } while(!verificador);

                /*
                    Cadastro da Data
                    Caso seja digitado ENTER, atribui a data atual para a venda
                    Caso seja feita um Input manual, a entrada deve obedecer o formato de data dd/MM/yyyy
                */
                System.out.printf("Digite a data de Venda do Produto (ENTER para Data Atual) no Formato: [dd/mm/yyyy]");
                System.out.print("\nData: ");
                

                verificador = false;
                do {

                    try{
                        String dataVenda = in.nextLine();

                        if(dataVenda.equals("")) { // Caso seja ENTER a entrada, atribui a data atual
                            dataVenda = LocalDate.now().format(formatter).toString();

                        } else if(!dataVenda.matches(padraoData)) {
                            System.out.println("\nO Formato de Data digitado nao eh valido");
                            System.out.print("\nData: ");
                            continue;
                        }  

                        novaVenda.setDataVenda(LocalDate.parse(dataVenda, formatter));
                        verificador = true;

                    } catch(DataInvalidaException ex) {
                        System.out.println(ex.getMessage());
                        System.out.print("\nData: ");
                    }

                } while(!verificador);

                // End do Realizar Vendas

                try {
                    novaVenda.finalizarVenda();

                    vendasRealizadas.add(novaVenda);
                    System.out.println("Venda Realizada com Sucesso!");
                } catch(QuantidadeNegativaOuZeroException ex) {
                    System.out.println(ex.getMessage());
                    System.out.println("N??o foi poss??vel finalizar a venda!");
                }

                System.out.println("\nPressioner ENTER para continuar");
                in.nextLine();
                voltarMenu();
            }
            
            /*
                Erro de Op????o - Op????o != 0 e opc??o != -1:
            */
            else if(opcao != 0 && opcao != -1) { // Verifi????o Extra para evitar aparecer duas mensagens de error na tela
                System.out.println("Op????o Inv??lida !!");
                SECONDS.sleep(1);
                voltarMenu();
            }

        } while(opcao != 0);

        /*
            Encerrar Programa - Op????o 0:
        */
        System.out.println("\nFim do Programa!");
        MILLISECONDS.sleep(1500);
        in.close();
    } // End Main

    // Func??o para dar um Clear no Console
    private static void cls() throws IOException, InterruptedException {
        
        // Limpa toda a tela, deixando novamente apenas o menu
        if (System.getProperty("os.name").contains("Windows"))
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        else
            System.out.print("\033[H\033[2J");
        
        System.out.flush();  
    }



     /* 
        Recebe uma entrada do usu??rio e verifica se cont??m somente caracteres num??ricos
         ?? Usado uma string para obter a entrada para facilitar a verifica????o 
         Caso o valor seja um numero, retorna o pr??prio valor 
         Caso seja um caractere alphanum??rico ou alphab??titco, Retorna zero como forma de sair do Menu
         N??o possui throw para exce????es, pois sempre vai retornar valores inteiros, seja -1 para n??o num??rico ou o proprio numero
     */
    private static int verificarEntradaParaMenu(String valor) {
        if(valor.matches("[0-9]*")) {
            return Integer.parseInt(valor);

        } else{
            return -1; // Retorna -1 como valor fixo, assim n??o atrapalha as op????es dos Menus do c??digo 
        }
    }

    // Fun????o de Retornar ao menu chamando o m??todo cls() para limpar o console
    private static void voltarMenu()  throws InterruptedException, IOException{
        System.out.println("Retornando ao Menu...");
        SECONDS.sleep(1);
        cls();
    }


    /*
        Fun????o pr??pria para evitar muita repeti????o de c??digo
        -> Recebe como par??metro um ArrayList do tipo Produto para exibir os dados de todos os produtos cadastrados
    */
    private static void listarProdutosCadastrados(List<Produto> produtosCadastrados) {
        // Primeiro exibe os cabe??alhos e uma barra para dividi-los
        System.out.println("\nProdutos Encontrados: \n");
        System.out.format(formatoDoSyouFormatTitulo, "Id", "Nome Produto", "Valor", "Estoque");
        System.out.print(dividirTelaProdutos);

        produtosCadastrados.stream()
        .forEach(p -> {
            
            System.out.println();
            System.out.format(formatoDoSysouFormatProdutos, p.getCodigo(), p.getNome(), p.getValor(), p.getQtdEstoque() );
        });

        System.out.print(dividirTelaProdutos);
        System.out.println();

        //
        /*
            Cria um Sum??rio dos valores de todos os produtos e armazena automaticamente a m??dia, min e max deles.
            Ap??s usa, usa o pr??prio para exibir na tela tais valores do produtos

        */
        DoubleSummaryStatistics resumo = produtosCadastrados.stream() 
            .collect(Collectors.summarizingDouble(Produto::getValor)); 
        //

        String valorMin = String.format("Valor Min: R$%.2f", resumo.getMin());
        String valorMax = String.format("Valor Max: R$%.2f", resumo.getMax());
        String valorAvg = String.format("Valor Avg: R$%.2f", resumo.getAverage());

        System.out.format(formatoRodapeProdutos, valorMin, valorMax, valorAvg);
    }


    /*
        -> Recebe como par??metro um ArrayList do tipo Venda para exibir o relat??rio de todas as vendas realizadas
    */
    private static void gerarRelatorioVendas(List<Venda> vendasRealizadas, LocalDate dataInicial, LocalDate dataFinal) throws IOException, InterruptedException{
        // Primeiro exibe os cabe??alhos e uma barra para dividi-los
        cls();
        System.out.printf("Vendas no Periodo: %s - %s\n\n", 
            dataInicial.format(formatter).toString(),
            dataFinal.format(formatter).toString());

    
        /*
            Cria um novo ArrayList para poder armazenar os dados recebidos
            e remover SOMENTE as datas que n??o atenderem as condi????es espec??ficas (Data inicial e Data Final), assim
            ?? so dar um Foreach em todas as datas restantes pois estas v??o ser as que atenderam as condi????es
            Ap??s testes, assim ?? muito mais simples e quer menos linha, uma vez q depois as vendas q ficarem ser??o as certas
        */
        List<Venda> novoArrayParaRelatorio = new ArrayList<>();
        novoArrayParaRelatorio.addAll(vendasRealizadas);
        novoArrayParaRelatorio.removeIf(v -> (v.getDataVenda().isAfter(dataFinal) || v.getDataVenda().isBefore(dataInicial)));

        // Caso n??o haja nenhuma venda neste per??odo, da esse retorno ao usu??rio
        if(novoArrayParaRelatorio.size() == 0){
            System.out.println("\nNao foi possivel encontrar nenhuma venda no periodo: ");
            System.out.printf("Data Inicial: %s \nData Final: %s", dataInicial.format(formatter).toString(),
                dataFinal.format(formatter).toString());
                return;
        }

        // Exibe o rodap??: Valor total de Vendas no per??odo especificado

        Double valorTotal = 0.0;

        // Antes de Exibir, ordena as Venda por data para faciliar a vizualiza????o
        // Organiza a data da mais antiga para a mais recente
        novoArrayParaRelatorio.sort(new CompararVendaPorData());

        /* 
            Padr??o para iniciar o formato da tabela com os requisitos:
            - Data Venda
            - Total (Valor unit??rio X Quantidade Vendido)
            - Id + Nome do Produto
            - Quantidade vendida
            - Valor Unit??rio do produto
        */
        System.out.format(formatoTituloRelatorioVenda, "Data", "Total (R$)", "Produto", "Qtd", "Valor Uni. (R$)");
        System.out.print(dividirTelaRelatorioVendas);

        for (Venda venda : novoArrayParaRelatorio) {

            System.out.println();
            // Usa um String.Format criado no in??cio como uma constante para exibir os dados
            System.out.format(formatoCorpoRelatorioVenda, 
                venda.getDataVenda().format(formatter).toString(), // Pega a Data da Venda 
                venda.getQtdProdutoVendido() * venda.getProdutoVendido().getValor(), // Multiplica QtdVendida * Valor produto para obter o total
                "#" + venda.getProdutoVendido().getCodigo() + " - " + venda.getProdutoVendido().getNome(), // Pega o id + nome do produto vendido
                venda.getQtdProdutoVendido(), // Pega a quantidade de vendas do produto
                venda.getProdutoVendido().getValor()); // Pega o valor unit??rio do produto
            
                valorTotal += venda.getQtdProdutoVendido() * venda.getProdutoVendido().getValor();
        }
        System.out.print(dividirTelaRelatorioVendas);
        System.out.printf("\n\tValor total de Vendas no Periodo: R$%.2f", valorTotal);

        //
        
        
    }


    /*
        Fun????o pr??pria para evitar muita repeti????o de c??digo
        -> Recebe como par??metro o id do produto requisitado e um ArrayList do tipo Produto para exibir os dados do produto 
            encontrado pela busca
    */
    private static Produto listarProdutoPorId(List<Produto> produtosCadastrados, int codigoProduto) throws NoSuchElementException{

        return produtosCadastrados.stream()
            .filter(p -> p.getCodigo() == codigoProduto)
            .findFirst()
            .get();
    }

    /*
        Procura se no Array j?? existe algum produto com o id cadastrado, se j?? possui, Exibe um erro e n??o permite cadastrar
        o produto com um c??digo repetido
    */
    private static boolean verificarCodigoRepetidoNoArray(List<Produto> produtosCadastrados, int codigo) {

        for (Produto produto : produtosCadastrados) {
            if(produto.getCodigo() == codigo) {
                return true;
            }
        }

        return false;
    }
}