package com.example.vendams.app;

import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.example.vendams.clienteHTTP.ProdutoInterMongoDB;
import com.example.vendams.clienteHTTP.VendaInterMongoDB;
import com.example.vendams.interfaces.ProdutoBDConnect;
import com.example.vendams.interfaces.VendaBDConnect;
import com.example.vendams.shared.Produto;
import com.example.vendams.shared.VendaDto;
import com.example.vendams.utility.CompararVendaPorData;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

public class ProgramaLoja {

    /*
        Sistema de vendas não está completo por enquanto
        falta terminar de resolver o problema ao fazer um post nas vendas para a API

    */
    
    
    //#region Constantes usadas para montar as "tabelas" ou outros atributos dependentes de constantes
    final static String dividirTelaRelatorioVendas = "\n-------------------------------------------------------------------------------------------------------------------";
    final static String dividirTelaProdutos = "\n-----------------------------------------------------------------------------------------------";
    final static String formatoDoSyouFormatTitulo = "%10s %40s %17s %17s";
    final static String formatoDoSysouFormatProdutos = "%10d %40s %17.2f %17d";
    final static String formatoRodapeProdutos = "%25s %25s %25s";
    final static String formatoRodapeProdutosVendidos = "%25s";
    final static DateTimeFormatter formatter = ofPattern("dd/MM/yyyy");
    final static String padraoData = "\\d{2}/\\d{2}/\\d{4}";
    final static String formatoTituloRelatorioVenda = "%15s %18s %40s %12s %21s";
    final static String formatoCorpoRelatorioVenda = "%15s %18.2f %40s %12d %21.2f";
    //#endregion


    /*
        Não consigo usar o ProdutoFeign de jeito nenhum dentro na main, por causa do @Autowired, que sempre retorna valores 
        nulos para a injeção de bean, msm tal sendo criado e especificado
        :3
    */
    private static ProdutoBDConnect produtoConnect = new ProdutoInterMongoDB();

    private static VendaBDConnect vendaConnect = new VendaInterMongoDB();

    public static void main (String[] args) throws Exception{
        
        // 
        List<Produto> produtosCadastrados = new ArrayList<>();
        List<VendaDto> vendasRealizadas = new ArrayList<>();
        
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
                System.out.println("3 - Adicionar estoque de um Produto existente");
                System.out.println("0 - Sair");
                System.out.print("\nOpcao: ");
                
                
                opcao = verificarEntradaParaMenu(in.nextLine());
                cls();

                // READ
                // Listar um Produto ou Procurar algum específico por Id
                if(opcao == 1) {

                    // Consome a API dos produtos para obter todos os produtos cadastrados,
                    // Se não houver nenhum presente no optional, retorna ao menu
                    List<Produto> listProdutos = new ArrayList<>();

                    try {
                        listProdutos = produtoConnect.obterTodosProdutos();

                    } catch(HttpClientErrorException ex) {
                        System.out.println("Serviço indisponível no Momento!");
                        voltarMenu();
                        continue;
                    }
                
                    if(listProdutos.isEmpty()) {
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

                       listarProdutosCadastrados(listProdutos); // Método para listar todos os produtos cadastrados 

                    } else { // Segue caso tenha digitado algum código válido


                        /*
                            Se chegou aqui, a entrada possui caracters numéricos válidos para buscar o código
                            Chama o método de verificação
                            1º Atribui para código a entrada do usuario
                            2º Busca com o método buscarProdutoPorId() que vai retornar a posição na lista que o produto se 
                                encontrada, caso não encontre nada, gera uma exceção de NullPointerException()
                        */
                        int codigoProduto = Integer.parseInt(verificacaoEntrada);
                        Optional<Produto> produtoEncontrado = Optional.empty();

                        try {
                            produtoEncontrado = produtoConnect.obterProdutoPorCodigo(codigoProduto);

                        } catch(HttpClientErrorException ex) {
                            System.out.println("Serviço indisponível");
                            voltarMenu();
                            continue;
                        } 

                        // Se o produto existir, o lista
                        if(produtoEncontrado.isPresent()) {
                            // Mesmo formato de Exibiçào da Lista de Produtos, porém adaptada para somente um Produto
                            System.out.println("\nProduto Encontrado: \n");
                            System.out.format(formatoDoSyouFormatTitulo, "Id", "Nome Produto", "Valor", "Estoque");
                            System.out.print(dividirTelaProdutos);

                            System.out.println();
                            System.out.format(formatoDoSysouFormatProdutos, produtoEncontrado.get().getCodigo(),
                                produtoEncontrado.get().getDescricao(), 
                                produtoEncontrado.get().getValor(), 
                                produtoEncontrado.get().getQuantidadeEstoque());
                            
                    
                            System.out.print(dividirTelaProdutos);
                            /////
                        } else {

                            System.out.printf("\nNenhum produto encontrado com o código: %d\n", codigoProduto);
                        }


                        
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

                            // Verificar se o código digitado é negativo:
                            if(codigo < 1) {
                                System.out.print("\nO Código deve ser > 0!");
                                System.out.print("\nCodigo: ");
                                continue;
                            }


                            // Verifica se já existe algum produto com este código, se sim, pede para digitar novamente
                            Optional<Produto> optional = produtoConnect.obterProdutoPorCodigo(codigo);

                            if(optional.isPresent()) {
                                System.out.println("Este codigo ja pertence a um produto, digite novamente !!");
                                System.out.print("\nCodigo: ");
                                continue;
                            }

                            
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

                        } catch(HttpClientErrorException ex) {

                            /*
                                Em caso de o resultado da busca não encontrar o produto, ele retorna a mesma exceção
                                de serviço indisponível, logo, usa-se o ENUM de HttpStatus para filtrar em cima da
                                exceção exata

                                NOT_ACCEPTABLE é o retorno definido em ProdutoController, como o status caso nenhum 
                                produto seja encontrado
                            */
                            switch(ex.getStatusCode()) {
                                case NOT_ACCEPTABLE:
                                    System.out.println("\nNenhum produto com código especificado!, Seguindo o cadastro...\n");
                                    verificador = true;
                                    produto.setCodigo(codigo);
                                    break;
                                
                                case NOT_FOUND:
                                    System.out.println("Serviço indisponível");
                                    verificador = false;
                                    break;
                                default:
                                    break;
                            }
                            break;
                        }
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
                        try{
                            
                            double valor = in.nextDouble();
                            in.nextLine();

                            // Verificar se o valor é negativo / diferente do mínimo permitido
                            if(valor < 0.01) {
                                System.out.println("O Valor deve ser positivo / maior que 0.01!!");
                                System.out.print("\nValor: ");
                                continue;
                            }

                            produto.setValor(valor);
                            verificador = true;

                        } catch(InputMismatchException ex) {
                            System.out.println("Digite Apenas numeros e ponto !!");
                            System.out.print("\nValor: ");
                            in.nextLine();
                        } 

                    } while(!verificador);
                    verificador = false;


                    System.out.println("\nDigite a Quantidade de estoque do Produto: ");
                    do {
                        try{
                            int quantidade = in.nextInt();
                            in.nextLine();

                            // Verificar se a quantidade é < 1
                            if(quantidade < 1) {
                                System.out.println("O Valor deve ser maior ou igual a 1!!");
                                System.out.print("\nQuantidade: ");
                                continue;
                            } 

                            produto.setQuantidadeEstoque(quantidade);
                            verificador = true;

                        } catch(InputMismatchException ex) {
                            System.out.println("Digite Apenas numeros !!");
                            System.out.print("\nQuantidade: ");
                            in.nextLine();
                        }

                    } while(!verificador);

                    Produto produtoCadastrado = new Produto();

                    try {
                        produtoCadastrado = produtoConnect.inserirNovoProduto(produto);

                        System.out.printf("\nProduto - %s - Cadastrado Com Sucesso!\n", produtoCadastrado.getDescricao());
                        in.nextLine();
                        SECONDS.sleep(1);

                    } catch(HttpClientErrorException ex) {

                        /*
                        Em caso de o resultado da busca não encontrar o produto, ele retorna a mesma exceção
                        de serviço indisponível, logo, usa-se o ENUM de HttpStatus para filtrar em cima da
                        exceção exata

                        NOT_ACCEPTABLE é o retorno definido em ProdutoController, como o status caso nenhum 
                        produto seja encontrado
                        */
                        switch(ex.getStatusCode()) {
                            case NOT_ACCEPTABLE:
                            /*
                                Retorna este status caso alguma validação do Serviço de Produto tenha sido violada
                                segue enviando as violações realizadas 
                            */
                                System.out.println("\n" + ex.getMessage() + "\n");

                                verificador = true;
                                break;
                            
                            case NOT_FOUND:
                                System.out.println("Serviço indisponível");
                                verificador = false;
                                break;
                            default:
                                break;
                        }
                    }

                    
                }
                
                // UPDATE
                // Permite atualizar a quantidade de estoque de um produto
                else if (opcao == 3) {
                    
                    List<Produto> listProdutos = new ArrayList<>();

                    try {
                        listProdutos = produtoConnect.obterTodosProdutos();

                    } catch(HttpClientErrorException ex) {
                        System.out.println("Serviço indisponível no Momento!");
                        voltarMenu();
                        continue;
                    }

                    if(listProdutos.isEmpty()) {
                        System.out.println("Não existem produtos para serem atualizados!");
                        voltarMenu();
                        continue;
                    }

                    // Se tudo ocorrer bem, segue para alterar o estoque do produto
                    boolean verificador = false;
                    String verificacaoEntrada;
                    Produto produtoEncontrado = new Produto();

                    // Buscar produto pelo ID
                    System.out.println("\nPara retornar ao menu, digites quaisquer caractere alfabético");
                    System.out.print("\nDigite a codigo do Produto: ");
                    do {
                        verificacaoEntrada = in.nextLine();
                        // Cuida de caso digitar algum caractere alfabético, retorna ao Menu
                        if(!verificacaoEntrada.matches("[0-9]*") 
                            || verificacaoEntrada.equals("")) {
                            break;
                        }

                        int codigo = Integer.parseInt(verificacaoEntrada);
                        
                        try {

                            Optional<Produto> optional = produtoConnect.obterProdutoPorCodigo(codigo);
                            produtoEncontrado = optional.get();

                            verificador = true;

                        } catch(HttpClientErrorException ex) {
                            switch(ex.getStatusCode()) {
                                case NOT_ACCEPTABLE:
                                    System.out.println("\nNao foi encontrado nenhum produto com o Codigo fornecido");
                                    System.out.println("Digite Novamente um código válido");
                                    System.out.print("\nCodigo: ");
                                    break;
                                
                                case NOT_FOUND:
                                    System.out.println("Serviço indisponível");
                                    verificador = true;
                                    break;
                                default:
                                    break;
                            }
                        }


                    } while(!verificador);

                    if(!verificador) {
                        voltarMenu();
                        continue;
                    }

                    verificador = false;
                    // QTD
                    System.out.println("\nPara retornar ao menu, digites quaisquer caractere alfabético");
                    System.out.print("Digite a Quantidade de estoque do Produto: ");
                    do {
                        verificacaoEntrada = in.nextLine();
                        // Cuida de caso digitar algum caractere alfabético, retorna ao Menu
                        if(!verificacaoEntrada.matches("[0-9]*") 
                            || verificacaoEntrada.equals("")) {
                            break;
                        }

                        int quantidade = Integer.parseInt(verificacaoEntrada);
                        
                        // Verificar se a quantidade é < 1
                        if(quantidade < 1) {
                            System.out.println("O Valor deve ser maior ou igual a 1!!");
                            System.out.print("\nQuantidade: ");
                            continue;
                        } 

                        produtoEncontrado.setQuantidadeEstoque(quantidade);

                        verificador = true;


                    } while(!verificador);

                    // Após, realizar o PUT
                    try {
                        produtoEncontrado = produtoConnect.atualizarProduto(produtoEncontrado);

                        System.out.printf("\nProduto - %s - Atualizado Com Sucesso!\n", produtoEncontrado.getDescricao());
                        exibirDadosDoProduto(produtoEncontrado);
                        in.nextLine();
                        SECONDS.sleep(1);

                    } catch(HttpClientErrorException ex) {

                        /*
                        Em caso de o resultado da busca não encontrar o produto, ele retorna a mesma exceção
                        de serviço indisponível, logo, usa-se o ENUM de HttpStatus para filtrar em cima da
                        exceção exata

                        NOT_ACCEPTABLE é o retorno definido em ProdutoController, como o status caso nenhum 
                        produto seja encontrado
                        */
                        switch(ex.getStatusCode()) {
                            case NOT_ACCEPTABLE:
                            /*
                                Retorna este status caso alguma validação do Serviço de Produto tenha sido violada
                                segue enviando as violações realizadas 
                            */
                                System.out.println("\n" + ex.getMessage() + "\n");

                                verificador = true;
                                in.nextLine();
                                break;
                            
                            case NOT_FOUND:
                                System.out.println("Serviço indisponível");
                                verificador = false;
                                break;
                            default:
                                break;
                        }
                    }

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
                    - Vendas (Detalhadas)
            */
            else if(opcao == 2) {

                List<VendaDto> vendas = vendaConnect.obterTodasVendas();

                if(vendas.isEmpty()) { // Retorna ao meno caso não haja nenhuma venda cadastrada ainda
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

                List<Produto> listProdutos = new ArrayList<>();

                try {
                    listProdutos = produtoConnect.obterTodosProdutos();

                } catch(HttpClientErrorException ex) {
                    System.out.println("Serviço indisponível no Momento!");
                    voltarMenu();
                    continue;
                } catch(HttpServerErrorException ex) {
                    System.out.println("Serviço indisponível no Momento!");
                    voltarMenu();
                    continue;
                }

                // Valida se existem produtos cadastrados
                if(listProdutos.size() == 0) {
                    System.out.println("Não existem produtos cadastrados para vender!!");
                    voltarMenu();
                    opcao = -1;
                    continue;
                }

                System.out.println("==========================");
                System.out.println("===== Realizar Venda =====");
                System.out.println("==========================");

                System.out.println("** Caso queira voltar, digite quaisquer caractere alfabéticos\n");
                
                //#region Inserir Produto Individual
                String verificacaoEntrada = ""; 
                boolean verificador = false;

                VendaDto novaVenda = new VendaDto();
                novaVenda.setProdutosVendidos(new ArrayList<>());

                Produto produtoVendido;

                /*
                    Como pode ser adicionado mais de um produto, esta lista serve para armzenar temporariamente
                    os produtos escolhidos, caso um mesmo produto seja escolhido novamente e o estoque não seja suficiente,
                    esta lista servirá para validar este estoque
                */
                List<Produto> produtosSelecionados = new ArrayList<>();

                /*
                    Vai definir se os produtos foram selecionados e se a venda pode ser realizada
                */
                boolean realizarVenda = false;

                /*
                    usada para se caso a quantidade desejada de um produto já existente na lista de venda seja 
                    inválida, ai caso o usuário deseje retornar ao menu, será usado esse validador
                */
                boolean quantidadeInvalida = false;

                /*
                    Serve para validar quando o serviço não estiver disponível e retornar ao menu
                */
                boolean servicoDisponivel = true;

                do {
                    cls();
                    realizarVenda = false;
                    verificador = false;

                    produtoVendido = new Produto();
                    //#region ID PRODUTO
                    System.out.println("Digite o Id do Produto:");

                    /*
                        Repetição para fazer a coleta se caso um código válido, inválidou ou caractere alfabético seja
                        digitado pelo usuário
                    */
                    do{
                        verificacaoEntrada = in.nextLine();

                        // Verifica se deve-se prosseguir para finalizar a venda
                        if(verificacaoEntrada.equals("finish")) {
                            realizarVenda = true;
                            verificador = false;
                            break;
                        }

                        // Cuida de caso digitar algum caractere alfabético, retorna ao Menu
                        if(!verificacaoEntrada.matches("[0-9]*") 
                            || verificacaoEntrada.equals("")) {
                            break;
                        }

                        

                        // Passa o valor digitado para "codigo"
                        int codigo = Integer.parseInt(verificacaoEntrada);

                        try{
                            
                            // Passo o codigo para a Stream e busca um produto com o código fornecido
                            Optional<Produto> optional = produtoConnect.obterProdutoPorCodigo(codigo);
                            produtoVendido = optional.get();

                            

                            verificador = true;

                            if (verificarProdutoExistenteNaVenda(produtosSelecionados, produtoVendido)) {

                                for (Produto produtoVerificacao : produtosSelecionados) {
                                    /* 
                                        Caso o produto ja tenha sido escolhido, verifica se o estoque dele na lista 
                                        temporária é > 0, caso não, ele não possui mais quantidade para ser vendido e
                                        deve ser escolhido outro produto
                                        
                                    */
                                    if(produtoVendido.getCodigo() == produtoVerificacao.getCodigo()) {
                                        if(produtoVerificacao.getQuantidadeEstoque() == 0) {
                                            System.out.printf("O produto - %s - escolhido não possui mais estoque", 
                                                produtoVerificacao.getDescricao());
                                            System.out.println("\nDigite o código de outro Produto ou digite \"finish\" para finalizar a venda");
                                            System.out.print("\nDigite: ");
                                            verificador = false;
                                            break;
                                        }
                                    }
                                }
                                continue;
                            } 

                            exibirDadosDoProduto(optional.get());

                        } catch(HttpClientErrorException ex) {
                            
                            switch(ex.getStatusCode()) {
                                case NOT_ACCEPTABLE:
                                    System.out.println("\nNao foi encontrado nenhum produto com o Codigo fornecido");
                                    System.out.println("Digite Novamente um código válido");
                                    System.out.print("\nCodigo: ");
                                    break;
                                
                                case NOT_FOUND:
                                    System.out.println("\nServiço indisponível");
                                    verificador = true;
                                    servicoDisponivel = false;
                                    break;
                                default:
                                    break;
                            }
                            
                        } catch(HttpServerErrorException ex) {
                            System.out.println("\nServiço indisponível");
                            verificador = false;
                            break;
                        } catch (NoSuchElementException ex) {
                            System.out.println("\nErro interno - Não foi possivel procurar o produto selecionado");
                            verificador = false;
                            // in.nextLine();
                            break;  
                        }


                    }while(!verificador);

                    /*
                        Se caso o produto selecionado não possua mais estoque e seja necessário prosseguir para finalizar
                        a venda, executa esse If e pula direto para finalizar a venda
                        um tratamento sera realizado para caso pule para "Finalizar venda" sem ter selecionado no minimo
                        1 produto, este ficará mais abaixo
                    */
                    if(realizarVenda) {
                        cls();
                        break;
                    }

                    if(!verificador || !servicoDisponivel) { // Saída para o Menu caso digite caracteres alfabéticos
                        voltarMenu();
                        break;
                    }

                    //#endregion

                    // Prossegue o cadastro das vendas
                    verificador = false;
                    quantidadeInvalida = false;

                    //#region QUANTIDADE VENDIDA
                    System.out.printf("Digite a quantidade de Venda do Produto \"%s\"", produtoVendido.getDescricao());
                    System.out.print("\nQuantidade: ");

                    do {
                        
                        verificacaoEntrada = in.nextLine();
                        // Cuida de caso digitar algum caractere alfabético, retorna ao Menu
                        if(!verificacaoEntrada.matches("[0-9]*") 
                            || verificacaoEntrada.equals("")) {
                            quantidadeInvalida = true;
                            break;
                        }

                        // Passa o valor digitado para "quantidadeEstoque"
                        int quantidade = Integer.parseInt(verificacaoEntrada);

                        // verifica se a quantidade deseja existe no produto atual
                        if(quantidade > produtoVendido.getQuantidadeEstoque()) {
                            System.out.printf("\nA quantidade solicitada do produto \"%s\" não está disponível\n", produtoVendido.getDescricao());
                            System.out.printf("Digite uma quantidade que seja igual ou menor a %d ou digite caracteres numéricos para sair\n", 
                                produtoVendido.getQuantidadeEstoque());
                            System.out.print("Digite: ");
                            verificador = false;
                            continue;
                        } 

                        // Verificar se o produto ja foi adicionado a venda e sua quantidade atual disponível
                        if(verificarProdutoExistenteNaVenda(produtosSelecionados, produtoVendido)) {

                            // Se o produto existir, sera feito uma validação se a quantidade desejada ainda persiste
                            for (Produto produtoVerificacao : produtosSelecionados) {
                                // Caso seja o mesmo código, prossegue
                                if(produtoVerificacao.getCodigo() == produtoVendido.getCodigo()) {

                                    // verifica se a quantidade deseja existe no produto atual
                                    if(quantidade > produtoVerificacao.getQuantidadeEstoque()) {
                                        System.out.printf("\nA quantidade solicitada do produto \"%s\" não está disponível\n", produtoVendido.getDescricao());
                                        System.out.printf("Digite uma quantidade que seja igual ou menor a %d ou digite caracteres numéricos para sair\n", 
                                            produtoVerificacao.getQuantidadeEstoque());
                                        System.out.print("Digite: ");
                                        verificador = false;
                                        break;
                                    } else {
                                        /*
                                            Caso a quantidade não ultrapasse o permitido,
                                            altera a nova quantidade na lista temporária e adiciona um novo
                                            produto à venda
                                        */
                                        int quantidadeAtual = produtoVerificacao.getQuantidadeEstoque();
    
                                        produtoVerificacao.setQuantidadeEstoque(quantidadeAtual - quantidade);

                                        produtoVendido.setQuantidadeEstoque(quantidade);

                                        // Adiciona à venda
                                        novaVenda.getProdutosVendidos().add(produtoVendido);

                                        verificador = true;
                                    }
                                } 
                            }

                        } else {
                            Produto produtoTemporario = new Produto();
                            produtoTemporario.setId(produtoVendido.getId());
                            produtoTemporario.setCodigo(produtoVendido.getCodigo());
                            produtoTemporario.setDescricao(produtoVendido.getDescricao());
                            produtoTemporario.setValor(produtoVendido.getValor());
                            produtoTemporario.setQuantidadeEstoque(produtoVendido.getQuantidadeEstoque());

                            int estoqueAtual = produtoTemporario.getQuantidadeEstoque();

                            // Define o estoque na lista temporária com a quantidade após a "venda"
                            produtoTemporario.setQuantidadeEstoque(estoqueAtual - quantidade);

                            // Adiciona à lista temporária  
                            produtosSelecionados.add(produtoTemporario);

                            /*
                                A Quantidade do estoque servirá para anotar quanto do produto foi vendido
                                na hora de realizar a venda e remover do estoque a quantidade vendida,
                                pega-se a quantidade de venda e se subtrai pelo estoque original do produto

                                5 - 2
                            */  
                            produtoVendido.setQuantidadeEstoque(quantidade);

                            // Adiciona à lista temporária
                            novaVenda.getProdutosVendidos().add(produtoVendido);
                            verificador = true;
                        }
                        
                        

                    } while(!verificador); 

                    //#endregion

                    // Sai do loop principal de inserção de produtos
                    if(quantidadeInvalida) {
                        voltarMenu();
                        break;
                    }


                    System.out.println("\nProduto adicionado à venda com sucesso!\n");
                    exibirDadosDoProdutoVendido(produtoVendido);

                    System.out.println("Siga as intruções e digite a opção válida:");
                    System.out.println("1 - Adicionar um novo produto a venda");
                    System.out.println("2 - Prosseguir para finalizar a venda");
                    System.out.println("3 - Listar o produtos selecionados até agora");
                    System.out.println("Digite qualquer caractere alfabético para retornar ao menu");
                    

                    // Valida se a entrada foi apenas de inteiro, se sim, segue para alguma opção válida
                    // Se não, retorna -1 e vai para o menu principal
                    do {
                        System.out.print("\nOpcao: ");
                        opcao = verificarEntradaParaMenu(in.nextLine());
                        
                        // ADD
                        // Adicionar Novo Produto
                        if(opcao == 1) {
                            verificador = true;
                            break;
                        }

                        // VENDER
                        // Prosseguir para finalizar a venda
                        else if(opcao == 2) {
                            verificador = false;
                            realizarVenda = true;
                            break;
                            
                        }

                        // LISTAR
                        // Lista os produtos adicionados até então
                        else if(opcao == 3) {
                            listarProdutosAtualmenteSelecionados(novaVenda.getProdutosVendidos());
                            System.out.println("\nAperte ENTER para prosseguir");
                            in.nextLine();
                            verificador = true;
                            
                        }

                        // RECARREGA
                        // Caso a opção seja inválida
                        else if(opcao != -1){
                            System.out.println("Opção inválida, digite novamente!");
                            verificador = true;
                        }

                        // RETORNA
                        // Caso a opção tenha sido um caractere numérico para voltar ao menu
                        else if(opcao == -1) {
                            voltarMenu();
                            verificador = false;
                            break;
                        }
                    }while(opcao != -1);


                } while(verificador);

                // Caso a saída tenha sido feito de maneira a retornar ao menu, executa o método
                if(quantidadeInvalida) {
                    voltarMenu();
                    continue;
                }

                if(realizarVenda) {

                    if(novaVenda.getProdutosVendidos().size() == 0) {
                        System.out.println("Nenhum produto foi adicionado à venda, retorne ao menu e tente novamente!");
                        System.out.println("Digite ENTER para continuar");
                        in.nextLine();
                        voltarMenu();
                        continue;
                    }

                    /*
                        Cadastro da Data
                        Caso seja digitado ENTER, atribui a data atual para a venda
                        Caso seja feita um Input manual, a entrada deve obedecer o formato de data dd/MM/yyyy
                    */
                    System.out.printf("Digite a data de Venda do Produto (ENTER para Data Atual) no Formato: [dd/mm/yyyy]");
                    System.out.print("\nData: ");
                    

                    verificador = false;
                    do {

                    
                        String dataVenda = in.nextLine();

                        if(dataVenda.equals("")) { // Caso seja ENTER a entrada, atribui a data atual
                            dataVenda = LocalDate.now().format(formatter).toString();

                        } else if(!dataVenda.matches(padraoData)) {
                            System.out.println("\nO Formato de Data digitado nao eh valido");
                            System.out.print("\nData: ");
                            continue;
                        } else {
                            // Verificação para evitar que a data da venda seja uma data posterior à data atual
                            LocalDate dataVerificacao = LocalDate.parse(dataVenda, formatter);
                            
                            if(dataVerificacao.isAfter(LocalDate.now())) {
                                System.out.println("\nA data não pode ser posterior à data atual!");
                                System.out.print("\nData: ");
                                continue;
                            }
                        }
                        novaVenda.setDataVenda(LocalDate.parse(dataVenda, formatter));
                        verificador = true;


                    } while(!verificador);
    
                    //#endregion 

                    // End do Realizar Vendas

                    try {


                        /*
                            A lista temporária de produtos guarda a informação de cada produto após sua adição a lista de vendas
                            ao chegar aqui, o jeito como estiver o produto na lista, será o novo estoque do produto, que será 
                            atualizado 1 a 1 para ser respectivo valor

                            Caso algum produto tenha o estoque 0, não será removido para evitar q outros produtos usem
                            o mesmo código, mas caso seja necessário adicionar + estoque ao produto, deve-se usar a função
                            do menu de produtos para isso

                            A cada vez que o ciclo rodar, o produto vendido será atualizado no estoque, se a qualquer momento

                            por enquanto não vou adicionar um método de backup caso o serviço seja encerrado durante este 
                            processo, ja que a venda não teria sido finalizada, deveria ser necessário retornar o estoque do 
                            produto, estou ajeitando umas cosa e dps se der eu faço e realizo o commit :3, só n pare o serviço 
                            durante esse processo de Baixa dos Produtos
                        */
                        novaVenda = vendaConnect.inserirNovaVenda(novaVenda);

                        for (Produto produtoFinal : produtosSelecionados) {
                            produtoConnect.atualizarProduto(produtoFinal);
                        }

                        // Realiza o calculo do total da venda
                        Double valorTotal = novaVenda.getProdutosVendidos().stream()
                            .mapToDouble(produto -> produto.getValor() * produto.getQuantidadeEstoque())
                            .sum();

                        novaVenda.setValorTotal(valorTotal);

                        // Venda aqui
                        System.out.println("Venda Realizada com Sucesso!");
                    } catch(HttpClientErrorException ex) {
                       
                        switch(ex.getStatusCode()) {
                            
                            case NOT_FOUND:
                                System.out.println("Não foi possível finalizar a venda!");
                                System.out.println("Serviço indisponível");
                                verificador = true;
                                break;
                            default:
                                break;
                        }
                        voltarMenu();
                        continue;

                    } catch(HttpServerErrorException ex) {
                        System.out.println("Serviço indisponível");
                        voltarMenu();
                        in.nextLine();
                        continue;
                    }

                    System.out.println("\nPressioner ENTER para continuar");
                    in.nextLine();
                    voltarMenu();
                    
                } 


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
    private static void gerarRelatorioVendas(List<VendaDto> vendasRealizadas, LocalDate dataInicial, LocalDate dataFinal) throws IOException, InterruptedException{
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
        List<VendaDto> novoArrayParaRelatorio = new ArrayList<>();
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
        Utilizado para poupar código e poder exibir o produto encontrado quando pesquisado pelo código 
    */
    private static void exibirDadosDoProduto(Produto produto) {
        System.out.println("\n** Produto Encontrado:\n");
        System.out.printf("Nome: %s\n", produto.getDescricao());
        System.out.printf("Valor: %.2f\n", produto.getValor());
        System.out.printf("Quantidade Estoque: %d\n\n", produto.getQuantidadeEstoque());
    }

    /*
        Utilizado para poupar código e poder exibir o produto encontrado quando vendido
    */
    private static void exibirDadosDoProdutoVendido(Produto produto) {
        System.out.println("\n** Produto Vendido:\n");
        System.out.printf("Nome: %s\n", produto.getDescricao());
        System.out.printf("Valor: %.2f\n", produto.getValor());
        System.out.printf("Quantidade Vendida: %d\n\n", produto.getQuantidadeEstoque());
    }

    /*
        Verifica se um produto ja foi adicionado a venda, se sim, retorna true
    */
    private static boolean verificarProdutoExistenteNaVenda(List<Produto> produtos, Produto produto) {
        for (Produto verificador : produtos) {
            // Caso possua algum produto repetido, faz a verificação do estoque
            if(verificador.getCodigo() == produto.getCodigo()) {
                return true;
            }
        
        }

        return false;
    } 


    /*
        Recebe uma lista dos produtos selecionados para exibir na tela
        diferente do outro método, este leva em conta não o valor médio dos produtos, mas o valor total de venda
        até então
    */
    private static void listarProdutosAtualmenteSelecionados(List<Produto> produtosSelecionados) {
        // Primeiro exibe os cabeçalhos e uma barra para dividi-los
        System.out.println("\nProdutos Selecionados: \n");
        System.out.format(formatoDoSyouFormatTitulo, "Id", "Nome Produto", "Valor", "Qtd Vendida");
        System.out.print(dividirTelaProdutos);

        produtosSelecionados.stream()
        .forEach(p -> {
            
            System.out.println();
            System.out.format(formatoDoSysouFormatProdutos, p.getCodigo(), p.getDescricao(), p.getValor(), p.getQuantidadeEstoque() );
        });

        System.out.print(dividirTelaProdutos);
        System.out.println();

        //
        /*
            Pega o valor e a quantidade de todos os produtos e soma para se obter o total da venda
            quantidade de estoque nesse caso se torna a quantidade q foi vendida
        */
        Double valorTotal = produtosSelecionados.stream()
            .mapToDouble(produto -> produto.getQuantidadeEstoque() * produto.getValor())
            .sum();
        //

        String valorTotalDaVenda = String.format("Valor Total: R$%.2f", valorTotal);
        
        System.out.format(formatoRodapeProdutosVendidos, valorTotalDaVenda);
        System.out.println();
    }
}