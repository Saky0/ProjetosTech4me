package com.example.produtoms.service;

import java.util.List;

import com.example.produtoms.shared.ProdutoDto;

public interface ProdutoService {
    //#region GETS

    // Obtem todos os produtos por um Get sem parâmetros
    List<ProdutoDto> obterTodosProdutos();
    //#endregion


    //#region POSTS

    // Inseri um novo Produto, mapeando o produto recebido para o objeto adequado
    ProdutoDto inserirNovoProduto(ProdutoDto dto);

    //#endregion


    // UPDATES
    ProdutoDto atualizarNovoProduto(String id, ProdutoDto dto);

    // DELETES
    void removerProdutoPorId();
}
