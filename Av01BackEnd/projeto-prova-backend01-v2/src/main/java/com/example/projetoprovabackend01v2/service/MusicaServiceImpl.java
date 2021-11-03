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


    @Override
    public List<MusicaDto> obterMusicas() {
        // TODO Auto-generated method stub
        List<MusicaDto> musicasDtos = repository.findAll().stream()
            .map(m -> mapper.map(m, MusicaDto.class))
            .collect(Collectors.toList());
        
        return musicasDtos;
    }

    @Override
    public MusicaDto criarMusica(MusicaDto musicaDto) {
        // TODO Auto-generated method stub
        Musica musicaNova = mapper.map(musicaDto, Musica.class);

        return mapper.map(musicaNova, MusicaDto.class);
    }

    @Override
    public MusicaDto atualizarMusica(String id, MusicaDto musicaDto) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void removerMusica(String id) {
        // TODO Auto-generated method stub
        repository.deleteById(id);
    }

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
    
}
