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
    @Query(value = "{'album': /?0/}")
    List<Musica> obterPorAlbum(String album);

    @Query("{'titulo': /?0/i}")
    List<Musica> obterPorTitulo(String palavra);
    
}
