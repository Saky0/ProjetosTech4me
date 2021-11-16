package com.example.produtoms.repository;

import com.example.produtoms.model.Produto;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository  extends MongoRepository<Produto, String> {
    
}
