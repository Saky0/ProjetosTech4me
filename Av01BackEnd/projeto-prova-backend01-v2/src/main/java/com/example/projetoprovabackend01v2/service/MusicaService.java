package com.example.projetoprovabackend01v2.service;

import java.util.List;
import java.util.Optional;

import com.example.projetoprovabackend01v2.shared.MusicaDto;

public interface MusicaService {
    // Obtém uma Lista Simples de Todas as músicas registradas
    List<MusicaDto> obterMusicas();

    // Inseri um Novo Objeto Musica no DB
    MusicaDto criarMusica(MusicaDto musicaDt);
    // Atualiza um Registro já existente
    MusicaDto atualizarMusica(String id, MusicaDto musicaDto);

    // Remove um musica por um Id especificado na requisição
    void removerMusica(String id);

    Optional<MusicaDto> obterMusicaPorId(String id);
    
    // Para obter uma lista de Musicas a partir de um Titulo, podendo ser partes do nome ou o nome inteiro
    // Neste caso, retorna todas as musicas que contenham o padrão especificado
    Optional<List<MusicaDto>> obterMusicaPorTitulo(String titulo);
    
    // No caso de um autor possui mais de uma música, retornar todas do album
    Optional<List<MusicaDto>> obterMusicasPorAlbum(String album);
}
