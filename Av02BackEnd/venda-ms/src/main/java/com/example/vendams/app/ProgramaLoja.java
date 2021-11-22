package com.example.vendams.app;

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
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.time.LocalDate.now;

import com.example.vendams.clienteHTTP.ProdutoFeignClient;
import com.example.vendams.clienteHTTP.VendaFeignClient;
import com.example.vendams.exceptions.*;
import com.example.vendams.shared.Produto;
import com.example.vendams.view.model.VendaResponse;
import com.example.vendams.utility.*;

import org.springframework.beans.factory.annotation.Autowired;


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

    @Autowired
    private static ProdutoFeignClient produtoClient;

    @Autowired
    private static VendaFeignClient vendaClient;

    public static void main (String[] args) throws InterruptedException, IOException, QuantidadeNegativaOuZeroException{
        
        // 
        List<Produto> produtosCadastrados = new ArrayList<>();
        List<VendaResponse> vendasRealizadas = new ArrayList<>();
 
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
                System.out.println("Digite Somente Números !!");
                SECONDS.sleep(1);
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

                    // Consome a API dos produtos para obter todos os produtos cadastrados,
                    // Se não houver nenhum presente no optional, retorna ao menu
                    List<Produto> listaProdutos = produtoClient.obterProdutos();

                    Optional<List<Produto>> optProdutos = Optional.of(produtosCadastrados);

                    if(!optProdutos.isPresent()) {
                        System.out.println("Não existem produtos cadastrados para serem listados !!");
                        SECONDS.sleep(1);
                        voltarMenu();
                        continue;
                    }
                    // Sempre antes  de listar quaisquer produto, organiza a lista por ordem crescente dos Id dos produtos
                    // - produtosCadastrados.sort(new CompararProdutosPorId());
                    /*
                        Entradas:
                        -> ENTER ou Caractere Alfabético: Exibe todos os Produtos Cadastrados
                            Neste caso, digitar um caractere não numérico prossegue com a mesma função do ENTER:
                            Listar todos os produtos cadastrados
                        -> Int: Procura um produto com o código indicado
                    */
                    System.out.print("\n-> Digite o codigo do produto para fazer uma busca do produto");
                    System.out.println("\n-> Aperte ENTER ou digite alguma letras para Listar Todos os produtos + relatorio");
                    System.out.print("\nCodigo: ");
                    String verificacaoEntrada = in.nextLine();


                    if(verificacaoEntrada.equals("") || !verificacaoEntrada.matches("[0-9]*")) { // Caso a entrada seja Apenas ENTER, lista todos existentes

                        listarProdutosCadastrados(optProdutos.get()); // Método para listar todos os produtos cadastrados 

                    } else { // Segue caso tenha digitado algum código válido


                        /*
                            Se chegou aqui, a entrada possui caracters numéricos válidos para buscar o código
                            Chama o método de verificação
                            1º Atribui para código a entrada do usuario
                            2º Busca com o método buscarProdutoPorId() que vai retornar a posição na lista que o produto se 
                                encontrada, caso não encontre nada, gera uma exceção de NullPointerException()
                        */
                        int codigoProduto = Integer.parseInt(verificacaoEntrada);

                        //
                        Produto produtoEncontrado = new Produto();

                        try{
                            produtoEncontrado = listarProdutoPorId(optProdutos.get(), codigoProduto);
                        } catch(NoSuchElementException ex) {
                            System.out.println("Nao foi encontrado nenhum produto com o Codigo: " + codigoProduto);
                            System.out.println("\nPressioner ENTER para sair");
                            in.nextLine();
                            voltarMenu();
                            continue;
                        }
                        // Mesmo formato de Exibiçào da Lista de Produtos, porém adaptada para somente um Produto
                        System.out.println("\nProduto Encontrado: \n");
                        System.out.format(formatoDoSyouFormatTitulo, "Id", "Nome Produto", "Valor", "Estoque");
                        System.out.print(dividirTelaProdutos);

                        System.out.println();
                        System.out.format(formatoDoSysouFormatProdutos, produtoEncontrado.getCodigo(),
                            produtoEncontrado.getDescricao(), produtoEncontrado.getValor(), produtoEncontrado.getQuantidadeEstoque() );
                        
                
                        System.out.print(dividirTelaProdutos);
                        /////
                    }


                    // Após Listar, fica em espera até o usuario Pressionar ENTER
                    System.out.println("\n\nPressioner ENTER para sair");
                    in.nextLine();
                }

                // CREATE
                // Inseri um novo produto no sistema
                else if(opcao == 2) {

                    System.out.println("=============================");
                    System.out.println("===== Cadastrar Produto =====");
                    System.out.println("=============================");

                    System.out.println("** Caso queira voltar, digite qualquer caractere alfabético ** \n");

                    System.out.println("Digite o Id do Produto:");
                    Produto produto = new Produto();

                    /* */
                    int codigo = 0;
                    boolean verificador = false;
                    do{
                        try{
                            codigo = in.nextInt();
                            in.nextLine();

                            // Verifica se já existe algum produto com este código, se sim, pede para digitar novamente
                            if(verificarCodigoRepetidoNoArray(produtosCadastrados, codigo)) {
                                System.out.println("Este codigo ja pertence a um produto, digite novamente !!");
                                System.out.print("\nCodigo: ");
                                continue;
                            }

                            produto.setCodigo(codigo);
                            verificador = true;

                        } catch(InputMismatchException ex) {
                            /*
                                Estrutura para verificar se o usuário digitou algum caractere alfabético para retornar ao 
                                Meno, caso sim, a função abaixo deixa verificador como false 
                                E um If retorna-o para o menu
                            */
                            in.nextLine();
                            verificador = false;
                            break;

                        } /* catch(CodigoNegativoException ex) {
                            /* 
                                Estrutura para tratar a possível exceção do usuário digitar um código que seja negativo
                            */ /* 
                            System.out.println(ex.getMessage());
                            System.out.print("\nCódigo: ");
                        } */

                    }while(!verificador);

                    if(!verificador) {
                        voltarMenu();
                        continue;
                    }
                    /* */

                    System.out.println("\nDigite o Nome do Produto: ");
                    produto.setDescricao(in.nextLine());

                    System.out.println("\nDigite o Valor do Produto: ");
                    verificador = false;
                    do {
                        /* try{
                            
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
                        } */

                    } while(!verificador);
                    verificador = false;


                    System.out.println("\nDigite a Quantidade de estoque do Produto: ");
                    do {
                        /* try{
                            produto.setQuantidadeEstoque(in.nextInt());
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
                        } */

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

                // Opção Inválida
                else if(opcao != 0) {
                    System.out.println("Opção Inválida !!");
                }

                voltarMenu(); // Após sair da opção 1 ou 2, ja executa o método
                opcao = -1;

            }

            /*
                Relatório - Opção 2:
                Gera relatório de:
                    - Produtos cadastrados
                    - Vendas (Detalhadas)
            */
            else if(opcao == 2) {

                if(vendasRealizadas.size() == 0) { // Retorna ao meno caso não haja nenhuma venda cadastrada ainda
                    System.out.println("Não existe nenhuma venda no momento para gerar Relatórios !!\n");
                    SECONDS.sleep(1);
                    voltarMenu();
                    continue;
                }

                System.out.println("=================================");
                System.out.println("====== Relatório de Vendas =====");
                System.out.println("=================================");
                System.out.println("Determine o período que esta contido as vendas");
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
                LocalDate dataFinal = LocalDate.parse("2007-12-03"); // Atribuição inicial para evitar erros de compilação
                do {
                    try{
                        String data = in.nextLine(); // Para recolher o ENTER caso deseja a data atual
                        if(data.equals("")) {
                            String formatoPadrao = LocalDate.now().format(formatter);
                            dataFinal = LocalDate.parse(formatoPadrao, formatter);
                            verificador = true;
                            continue;
                        }

                        // Verificação para impedir que a data final se antes da data inicial
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
                    Após definir o intervalo das vendas (Não pode exceder a data atual)
                    Chama o método que constrói a "tabela" com o relatório
                */
                gerarRelatorioVendas(vendasRealizadas, dataInicial, dataFinal);
                

                // End Gerar Relatórios de Vendas
                System.out.println("\n\nPressioner ENTER para Sair");
                in.nextLine();
                voltarMenu();
                

            }
            
            /*
                Vendas - Opção 3:
                Inserir um novo Registro de Venda no Sistema
            */
            else if(opcao == 3) {

                if(produtosCadastrados.size() == 0) {
                    System.out.println("Não existem produtos cadastrados para vender!!");
                    voltarMenu();
                    opcao = -1;
                    continue;
                }

                System.out.println("==========================");
                System.out.println("===== Realizar Venda =====");
                System.out.println("==========================");

                System.out.println("** Caso queira voltar, digite quaisquer caractere alfabético ** \n");

                System.out.println("Digite o Id do Produto:");


                
                String verificacaoEntrada = ""; 
                boolean verificador = false;
                VendaResponse novaVenda = new VendaResponse();

                /*
                    Repetição para fazer a coleta se caso um código válido, inválidou ou caractere alfabético seja
                    digitado pelo usuário
                */
                do{
                    try{
                        verificacaoEntrada = in.nextLine();
                        // Cuida de caso digitar algum caractere alfabético, retorna ao Menu
                        if(!verificacaoEntrada.matches("[0-9]*") || verificacaoEntrada.equals("")) {
                            break;
                        }

                        // Passa o valor digitado para "codigo"
                        int codigo = Integer.parseInt(verificacaoEntrada);

                        // Passo o codigo para a Stream e busca um produto com o código fornecido
                        /* novaVenda.setProdutoVendido(produtosCadastrados.stream()
                        .filter(p -> p.getCodigo() == codigo)
                        .findFirst()
                        .get());  */
                        
                        verificador = true;
                    } catch(NoSuchElementException ex) {
                        System.out.println("\nNao foi encontrado nenhum produto com o Codigo fornecido");
                        System.out.println("Digite Novamente um código válido");
                        System.out.print("\nCodigo: ");
                        
                    } 

                }while(!verificador);

                if(!verificador) { // Saída para o Menu caso digite caracteres alfabéticos
                    voltarMenu();
                    continue;
                }

                // Prossegue o cadastro das vendas
                verificador = false;

                //System.out.printf("Digite a quantidade de Venda do Produto \"%s\"", novaVenda.getProdutoVendido().getNome());
                System.out.print("\nQuantidade: ");
               /*  do {
                    try { 
                
                        novaVenda.setQtdProdutoVendido(in.nextInt());
                        in.nextLine();

                        verificador = true;
                        
                    } catch(InputMismatchException ex) {
                        System.out.println("Digite Somente Números !!");
                        SECONDS.sleep(1);
                        in.nextLine();

                    } catch(QuantidadeNegativaOuZeroException ex) {
                        System.out.println(ex.getMessage());
                        System.out.print("\nQuantidade: ");
                        in.nextLine();
                    }

                } while(!verificador); */

                /*
                    Cadastro da Data
                    Caso seja digitado ENTER, atribui a data atual para a venda
                    Caso seja feita um Input manual, a entrada deve obedecer o formato de data dd/MM/yyyy
                */
                System.out.printf("Digite a data de Venda do Produto (ENTER para Data Atual) no Formato: [dd/mm/yyyy]");
                System.out.print("\nData: ");
                

                verificador = false;
                /* do {

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
 */
                // End do Realizar Vendas

                /* try {
                    //novaVenda.finalizarVenda();

                    vendasRealizadas.add(novaVenda);
                    System.out.println("Venda Realizada com Sucesso!");
                } catch(QuantidadeNegativaOuZeroException ex) {
                    System.out.println(ex.getMessage());
                    System.out.println("Não foi possível finalizar a venda!");
                } */

                System.out.println("\nPressioner ENTER para continuar");
                in.nextLine();
                voltarMenu();
            }
            
            /*
                Erro de Opção - Opção != 0 e opcão != -1:
            */
            else if(opcao != 0 && opcao != -1) { // Verifição Extra para evitar aparecer duas mensagens de error na tela
                System.out.println("Opção Inválida !!");
                SECONDS.sleep(1);
                voltarMenu();
            }

        } while(opcao != 0);

        /*
            Encerrar Programa - Opção 0:
        */
        System.out.println("\nFim do Programa!");
        MILLISECONDS.sleep(1500);
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
        Recebe uma entrada do usuário e verifica se contém somente caracteres numéricos
         É Usado uma string para obter a entrada para facilitar a verificação 
         Caso o valor seja um numero, retorna o próprio valor 
         Caso seja um caractere alphanumérico ou alphabétitco, Retorna zero como forma de sair do Menu
         Não possui throw para exceções, pois sempre vai retornar valores inteiros, seja -1 para não numérico ou o proprio numero
     */
    private static int verificarEntradaParaMenu(String valor) {
        if(valor.matches("[0-9]*")) {
            return Integer.parseInt(valor);

        } else{
            return -1; // Retorna -1 como valor fixo, assim não atrapalha as opções dos Menus do código 
        }
    }

    // Função de Retornar ao menu chamando o método cls() para limpar o console
    private static void voltarMenu()  throws InterruptedException, IOException{
        System.out.println("Retornando ao Menu...");
        SECONDS.sleep(1);
        cls();
    }


    /*
        Função própria para evitar muita repetição de código
        -> Recebe como parâmetro um ArrayList do tipo Produto para exibir os dados de todos os produtos cadastrados
    */
    private static void listarProdutosCadastrados(List<Produto> produtosCadastrados) {
        // Primeiro exibe os cabeçalhos e uma barra para dividi-los
        System.out.println("\nProdutos Encontrados: \n");
        System.out.format(formatoDoSyouFormatTitulo, "Id", "Nome Produto", "Valor", "Estoque");
        System.out.print(dividirTelaProdutos);

        produtosCadastrados.stream()
        .forEach(p -> {
            
            System.out.println();
            System.out.format(formatoDoSysouFormatProdutos, p.getCodigo(), p.getDescricao(), p.getValor(), p.getQuantidadeEstoque() );
        });

        System.out.print(dividirTelaProdutos);
        System.out.println();

        //
        /*
            Cria um Sumário dos valores de todos os produtos e armazena automaticamente a média, min e max deles.
            Após usa, usa o próprio para exibir na tela tais valores do produtos

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
        -> Recebe como parâmetro um ArrayList do tipo Venda para exibir o relatório de todas as vendas realizadas
    */
    private static void gerarRelatorioVendas(List<VendaResponse> vendasRealizadas, LocalDate dataInicial, LocalDate dataFinal) throws IOException, InterruptedException{
        // Primeiro exibe os cabeçalhos e uma barra para dividi-los
        cls();
        System.out.printf("Vendas no Periodo: %s - %s\n\n", 
            dataInicial.format(formatter).toString(),
            dataFinal.format(formatter).toString());

    
        /*
            Cria um novo ArrayList para poder armazenar os dados recebidos
            e remover SOMENTE as datas que não atenderem as condições específicas (Data inicial e Data Final), assim
            é so dar um Foreach em todas as datas restantes pois estas vão ser as que atenderam as condições
            Após testes, assim é muito mais simples e quer menos linha, uma vez q depois as vendas q ficarem serão as certas
        */
        List<VendaResponse> novoArrayParaRelatorio = new ArrayList<>();
        novoArrayParaRelatorio.addAll(vendasRealizadas);
        novoArrayParaRelatorio.removeIf(v -> (v.getDataVenda().isAfter(dataFinal) || v.getDataVenda().isBefore(dataInicial)));

        // Caso não haja nenhuma venda neste período, da esse retorno ao usuário
        if(novoArrayParaRelatorio.size() == 0){
            System.out.println("\nNao foi possivel encontrar nenhuma venda no periodo: ");
            System.out.printf("Data Inicial: %s \nData Final: %s", dataInicial.format(formatter).toString(),
                dataFinal.format(formatter).toString());
                return;
        }

        // Exibe o rodapé: Valor total de Vendas no período especificado

        Double valorTotal = 0.0;

        // Antes de Exibir, ordena as Venda por data para faciliar a vizualização
        // Organiza a data da mais antiga para a mais recente
        novoArrayParaRelatorio.sort(new CompararVendaPorData());

        /* 
            Padrão para iniciar o formato da tabela com os requisitos:
            - Data Venda
            - Total (Valor unitário X Quantidade Vendido)
            - Id + Nome do Produto
            - Quantidade vendida
            - Valor Unitário do produto
        */
        System.out.format(formatoTituloRelatorioVenda, "Data", "Total (R$)", "Produto", "Qtd", "Valor Uni. (R$)");
        System.out.print(dividirTelaRelatorioVendas);

        /* for (VendaResponse venda : novoArrayParaRelatorio) {

            System.out.println();
            // Usa um String.Format criado no início como uma constante para exibir os dados
            System.out.format(formatoCorpoRelatorioVenda, 
                venda.getDataVenda().format(formatter).toString(), // Pega a Data da Venda 
                venda.getQtdProdutoVendido() * venda.getProdutoVendido().getValor(), // Multiplica QtdVendida * Valor produto para obter o total
                "#" + venda.getProdutoVendido().getCodigo() + " - " + venda.getProdutoVendido().getNome(), // Pega o id + nome do produto vendido
                venda.getQtdProdutoVendido(), // Pega a quantidade de vendas do produto
                venda.getProdutoVendido().getValor()); // Pega o valor unitário do produto
            
                valorTotal += venda.getQtdProdutoVendido() * venda.getProdutoVendido().getValor();
        } */
        System.out.print(dividirTelaRelatorioVendas);
        System.out.printf("\n\tValor total de Vendas no Periodo: R$%.2f", valorTotal);

        //
        
        
    }


    /*
        Função própria para evitar muita repetição de código
        -> Recebe como parâmetro o id do produto requisitado e um ArrayList do tipo Produto para exibir os dados do produto 
            encontrado pela busca
    */
    private static Produto listarProdutoPorId(List<Produto> produtosCadastrados, int codigoProduto) throws NoSuchElementException{

        return produtosCadastrados.stream()
            .filter(p -> p.getCodigo() == codigoProduto)
            .findFirst()
            .get();
    }

    /*
        Procura se no Array já existe algum produto com o id cadastrado, se já possui, Exibe um erro e não permite cadastrar
        o produto com um código repetido
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