package com.example.projetoprovabackend01v2.service;

import java.util.List;
import java.util.Optional;

import com.example.projetoprovabackend01v2.shared.MusicaDto;

public interface MusicaService {
    List<MusicaDto> obterMusicas();
    MusicaDto criarMusica(MusicaDto musicaDt);
    MusicaDto atualizarMusica(String id, MusicaDto musicaDto);
    void removerMusica(String id);
    Optional<MusicaDto> obterMusicaPorId(String id);
}
