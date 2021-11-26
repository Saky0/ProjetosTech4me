package com.example.vendams.interfaces;

import java.util.List;
import java.util.Optional;

import com.example.vendams.shared.Produto;

public interface ProdutoBDConnect {
    /*
        A interface ProdutoBDConnect serve para implementar os métodos em quaisquer tipo de conexão com o BD
        podendo assim alterar a ClasseImpl com o tipo de conexão desejada e mantendo o retorno e parâmetros 
        dos métodos
    */
    List<Produto> obterTodosProdutos();

    Optional<Produto> obterProdutoPorId(String id);

    Optional<Produto> obterProdutoPorCodigo(int codigo);

    Produto inserirNovoProduto(Produto produtoNovo) throws Exception;

    void removerProduto(String id);

    Produto atualizarProduto(Produto produtoAtualizado) throws Exception;
}
