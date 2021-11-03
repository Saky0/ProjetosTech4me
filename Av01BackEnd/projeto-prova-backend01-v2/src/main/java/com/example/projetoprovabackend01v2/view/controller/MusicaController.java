package com.example.projetoprovabackend01v2.view.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.projetoprovabackend01v2.service.MusicaService;
import com.example.projetoprovabackend01v2.shared.MusicaDto;
import com.example.projetoprovabackend01v2.view.model.MusicaRequest;
import com.example.projetoprovabackend01v2.view.model.MusicaResponse;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;




@RestController
@RequestMapping("/api/musicas")
public class MusicaController {
    
    @Autowired
    private MusicaService service;

    private ModelMapper mapper = new ModelMapper();

    //#region @Gets
    @GetMapping
    public ResponseEntity<List<MusicaResponse>> obterMusicas() {
        List<MusicaResponse> musicasEncontradas = service.obterMusicas()
            .stream()
            .map(musica -> mapper.map(musica, MusicaResponse.class))
            .collect(Collectors.toList());

        return new ResponseEntity<>(musicasEncontradas, HttpStatus.OK);
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<MusicaResponse> obterPorId(@PathVariable String id) {
        Optional<MusicaDto> musicaEncontrada = service.obterMusicaPorId(id);

        if(musicaEncontrada.isPresent()) {
            return new ResponseEntity<>(mapper.map(musicaEncontrada.get(), MusicaResponse.class), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    //#endregion
    
    //#region @Posts

    @PostMapping    
    public ResponseEntity<MusicaResponse> criarMusica(@RequestBody MusicaRequest MusicaNova) {
        //TODO: process POST request
        MusicaDto dto = mapper.map(MusicaNova, MusicaDto.class);

        return new ResponseEntity<>(mapper.map(dto, MusicaResponse.class), HttpStatus.OK);
    }

    //#endregion
}
