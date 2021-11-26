package com.example.vendams.repository;

import java.time.LocalDate;
import java.util.List;

import com.example.vendams.model.Venda;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VendaRepository extends MongoRepository<Venda, String> {

    // @Query(value = "{'dataVenda': {$not: {$lt: ?0, $gt: ?1} }}")
    /*
        No final o que agt fez não funcionou, ele retorna todas as vendas independente da data :v
        Acabei optando por fzr o obterVendaPorPeriodo de maneira mais "Rústica", obtendo
        a lista completa de vendas e filtrando dentro do
    */
    @Query(value = "{'dataVenda':{$not: {$lt: ?0, $gt: ?1} }}")
    List<Venda> obterVendaPorPeriodo(LocalDate dataInicial, LocalDate dataFinal);
}
