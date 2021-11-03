package com.example.projetoprovabackend01v2.repository;

import com.example.projetoprovabackend01v2.model.Musica;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MusicaRepository extends MongoRepository<Musica, String> {
    
}
