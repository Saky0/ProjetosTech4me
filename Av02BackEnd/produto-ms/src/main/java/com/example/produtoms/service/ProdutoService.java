package com.example.produtoms.service;

import java.util.List;
import java.util.Optional;

import com.example.produtoms.shared.ProdutoDto;

public interface ProdutoService {
    //#region GETS

    // Obtem todos os produtos por um Get sem parâmetros
    List<ProdutoDto> obterTodosProdutos();

    // Obtem um produto específico por meio de seu Id
    Optional<ProdutoDto> obterProdutoPorId(String id);

    // Obtem uma lista de produtos por meio de um nome em comum
    Optional<List<ProdutoDto>> obterPorNomeEmComum(String nome);


    //#endregion


    //#region POSTS

    // Inseri um novo Produto, mapeando o produto recebido para o objeto adequado
    ProdutoDto inserirNovoProduto(ProdutoDto dto);

    
    //#endregion


    //#region PUTS

    // UPDATES
    ProdutoDto atualizarNovoProduto(String id, ProdutoDto dto);

    //#endregion

    //#region DELETES

    // DELETES
    void removerProdutoPorId(String id);

    //#endregion
}
