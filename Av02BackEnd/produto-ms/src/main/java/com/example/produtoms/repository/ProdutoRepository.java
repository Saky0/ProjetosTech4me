package com.example.produtoms.repository;

import java.util.List;

import com.example.produtoms.model.Produto;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository  extends MongoRepository<Produto, String> {

    /*
        Utiliza um nome passado pelo usuário para encontrar uma lista de produtos que possuam uma descrição (Nome) 
        em comum
    */
    @Query("{'descricao': {$regex: /?0/, $options: 'i'}}")
    List<Produto> obterPorNomeEmComum(String nome);
    
}
