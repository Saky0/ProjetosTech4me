package com.example.projetoprovabackend01v2.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.projetoprovabackend01v2.model.Musica;
import com.example.projetoprovabackend01v2.repository.MusicaRepository;
import com.example.projetoprovabackend01v2.shared.MusicaDto;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MusicaServiceImpl implements MusicaService{

    @Autowired
    private MusicaRepository repository;

    private ModelMapper mapper = new ModelMapper();

    /*
        Usando os métodos da Interface MusicaRepository, realiza o CRUD E algumas funções extras, como:
            1. Obter por album (Uma lista)
            2. Obter por nome
            3. Contagem total de registros
    */

    // Retorna uma Lista de todas as músicas
    @Override
    public List<MusicaDto> obterMusicas() {
        // TODO Auto-generated method stub
        List<MusicaDto> musicasDtos = repository.findAll().stream()
            .map(m -> mapper.map(m, MusicaDto.class))
            .collect(Collectors.toList());
        
        return musicasDtos;
    }

    // Inseri uma nova música
    @Override
    public MusicaDto criarMusica(MusicaDto musicaDto) {
        // TODO Auto-generated method stub
        Musica musicaNova = mapper.map(musicaDto, Musica.class);

        return mapper.map(repository.save(musicaNova), MusicaDto.class);
    }

    // Atualiza uma música ja existente
    @Override
    public MusicaDto atualizarMusica(String id, MusicaDto musicaDto) {
        // TODO Auto-generated method stub
        musicaDto.setId(id);
        Musica musica = mapper.map(musicaDto, Musica.class);

        return mapper.map(repository.save(musica), MusicaDto.class);
    }

    // Remove uma musica a partir de um Id
    @Override
    public void removerMusica(String id) {
        // TODO Auto-generated method stub
        repository.deleteById(id);
    }

    // Obtem uma música Especifica a partir do Id gerado auto. do MongoDb para cada Documento
    @Override
    public Optional<MusicaDto> obterMusicaPorId(String id) {
        // TODO Auto-generated method stub
        Optional<Musica> musicaOpt = repository.findById(id);

        if(musicaOpt.isPresent()) { 
            MusicaDto dto = mapper.map(musicaOpt.get(), MusicaDto.class);
            return Optional.of(dto);
        }

        return Optional.empty();
    }

    // Usa o método criado no MusicaRepo. para obter Lista de Musicas por Titulo
    @Override
    public Optional<List<MusicaDto>> obterMusicaPorTitulo(String Titulo) {
        // TODO Auto-generated method stub
        return null;
    }

    // Usa o método criado no MusicaRepo. para obter uma lista de músicas sobre um album específico
    /*
        Pontos importante:
            1. Recebe a lista do Repo e armazena antes de passar para o Optional, pois ao armazenar diretamente como em:
                Optional<List<Musica>> musicasPorAlbum = repository.obterPorAlbum(album);
            Os dados são perdidos e na hora de consumir a API, os valores chegam Null, esta alteração ocorre no Controller
            Para evitar o Tal mesmo problema

            Em outros Lugares isso será aplicado também
    */
    @Override
    public Optional<List<MusicaDto>> obterMusicasPorAlbum(String album) {
        // TODO Auto-generated method stub
        List<Musica> musicasPorAlbum = repository.obterPorAlbum(album);

        Optional<List<Musica>> optionalMusicasPorAlbum = Optional.of(musicasPorAlbum);

        if(optionalMusicasPorAlbum.isPresent()) {
            return Optional.of(musicasPorAlbum 
                .stream()
                .map(musica -> mapper.map(musica, MusicaDto.class))
                .collect(Collectors.toList()));
        }

        return Optional.empty();
    }
    
}
