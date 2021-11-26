package com.example.vendams.clienteHTTP;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.vendams.interfaces.ProdutoBDConnect;
import com.example.vendams.shared.Produto;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class ProdutoInterMongoDB implements ProdutoBDConnect{

    private RestTemplate resTemplate;
    /*
        Todos os métodos lançam uma exceção para ser tratada dentro do programa e assim poder
        retorna uma mensagem específica no caso de erros e retornar para o menu
    */
    
    @Override
    public List<Produto> obterTodosProdutos() throws HttpClientErrorException {
        // TODO Auto-generated method stub
        resTemplate = new RestTemplate();

        List<Produto> produtosEncontrados = resTemplate.exchange("http://localhost:8011/produto-ms/api/produtos", 
            HttpMethod.GET, 
            null, 
            new ParameterizedTypeReference<List<Produto>>() {}).getBody();

        if(!produtosEncontrados.isEmpty()) {
            return produtosEncontrados.stream()
            .sorted(Comparator.comparing(Produto::getCodigo))
            .collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    @Override
    public Optional<Produto> obterProdutoPorId(String id) throws HttpClientErrorException {
        // TODO Auto-generated method stub
        resTemplate = new RestTemplate();

        // Caso não possua um retorno, trata a exceção
        Optional<Produto> optional = Optional.empty();
        try {
            optional = Optional.of(resTemplate.getForObject("http://localhost:8011/produto-ms/api/produtos/idp/" + id, 
                Produto.class));
        } catch(HttpServerErrorException ex) {
            return Optional.empty();
        } 
        
        if(optional.isPresent()) {
            return optional;
        }

        return Optional.empty();
    }

    @Override
    public Optional<Produto> obterProdutoPorCodigo(int codigo) throws HttpClientErrorException {
        // TODO Auto-generated method stub
        resTemplate = new RestTemplate();

        Optional<Produto> optional = Optional.empty();
        // Caso não possua um retorno, trata a exceção
        try {
            optional = Optional.of(resTemplate.getForObject("http://localhost:8011/produto-ms/api/produtos/codigo/" + codigo, 
                Produto.class));
        } catch(HttpServerErrorException ex) {
            return Optional.empty();
        } 

        if(optional.isPresent()) {
            return optional;
        }

        return Optional.empty();
    }

    @Override
    public Produto inserirNovoProduto(Produto produtoNovo) throws JSONException, HttpServerErrorException, HttpClientErrorException{
        // TODO Auto-generated method stub
        resTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        JSONObject jsonObject = new JSONObject();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        String apiUrl = "http://localhost:8011/produto-ms/api/produtos";

        jsonObject.put("codigo", produtoNovo.getCodigo());
        jsonObject.put("descricao", produtoNovo.getDescricao());
        jsonObject.put("valor", produtoNovo.getValor());
        jsonObject.put("quantidadeEstoque", produtoNovo.getQuantidadeEstoque());

        HttpEntity<String> request = new HttpEntity<String>(jsonObject.toString(), headers);
        
        Produto p = resTemplate.postForObject(apiUrl, request, Produto.class);

        return p;
    }

    @Override
    public void removerProduto(String id) throws HttpClientErrorException{
        // TODO Auto-generated method stub
        resTemplate = new RestTemplate();

        resTemplate.delete("http://localhost:8011/produto-ms/api/produtos/" + id);
    }

    /*
        Sobre um feedback da prova de POO sobre o produto com estoque = 0 não ser removido
        para não precisar remover um produto pq ele acabou o estoque, e evitar q aconteça:

            -> Pão tinha código 1, foi vendido e agora na venda tem um produto com código 1, que é o pão

            -> Para evitar q outro produto assuma o código 1, e não faça sentido na venda, o produto permanece no estoque
                mas com a adicionar novos no estoque
    */
    @Override
    public Produto atualizarProduto(Produto produtoAtualizado) throws JSONException, HttpServerErrorException, HttpClientErrorException {
        // TODO Auto-generated method stub
        resTemplate = new RestTemplate();

        String apiUrl = "http://localhost:8011/produto-ms/api/produtos/" + produtoAtualizado.getId();

        Map<String, String> params = new HashMap<>();
        params.put("id", produtoAtualizado.getId());

        resTemplate.put(apiUrl, produtoAtualizado, params);
        
        return produtoAtualizado;
    }

}
