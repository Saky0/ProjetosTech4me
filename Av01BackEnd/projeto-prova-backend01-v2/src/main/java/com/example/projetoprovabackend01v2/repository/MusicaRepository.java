package com.example.projetoprovabackend01v2.repository;

import java.util.List;

import com.example.projetoprovabackend01v2.model.Musica;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MusicaRepository extends MongoRepository<Musica, String> {

    /*
        Query específica para Obter no DB uma lista de Documentos que possuam o 'album' especificado dentro
        do parâmetro
    */
    @Query(value = "{'album': { $regex: /?0/, $options: 'i'}}")
    List<Musica> obterPorAlbum(String album);

    /*  
        Obtem uma lista de musicas por um nome fornecido, podendos ser a musica exata ou alguma que possua os 
        mesmo caracteres 
    */
    @Query("{'titulo': { $regex: /?0/, $options: 'i'}}")
    List<Musica> obterPorTitulo(String titulo);
    
}
