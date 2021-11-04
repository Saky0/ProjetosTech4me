package com.example.projetoprovabackend01v2.view.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.example.projetoprovabackend01v2.service.MusicaService;
import com.example.projetoprovabackend01v2.shared.MusicaDto;
import com.example.projetoprovabackend01v2.view.model.MusicaRequest;
import com.example.projetoprovabackend01v2.view.model.MusicaResponse;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    //Retorna Uma lista com todas as músicas existentes
    @GetMapping
    public ResponseEntity<List<MusicaResponse>> obterMusicas() {
        List<MusicaResponse> musicasEncontradas = service.obterMusicas()
            .stream()
            .map(musica -> mapper.map(musica, MusicaResponse.class))
            .collect(Collectors.toList());

        return new ResponseEntity<>(musicasEncontradas, HttpStatus.OK);
    }

    // A partir do Id passado, obtem um objeto específico
    @GetMapping(value="/{id}")
    public ResponseEntity<MusicaResponse> obterPorId(@PathVariable String id) {
        Optional<MusicaDto> musicaEncontrada = service.obterMusicaPorId(id);

        if(musicaEncontrada.isPresent()) {
            return new ResponseEntity<>(mapper.map(musicaEncontrada.get(), MusicaResponse.class), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Obtém uma Lista de Musicas a partir de um 'album' especificado nos parâmetros da requisição
    // Segue o mesmo conceito/alteração necessária feito no ServiceImpl
    @GetMapping(value="/album/{album}")
    public ResponseEntity<List<MusicaResponse>> obterMusicasPorAlbum (@PathVariable String album) {
        List<MusicaDto> musicasPorAlbumDto = service.obterMusicasPorAlbum(album).get();

        Optional<List<MusicaDto>> optionalMusicasPorAlbum = Optional.of(musicasPorAlbumDto);

        if(optionalMusicasPorAlbum.isPresent()) {

            List<MusicaResponse> listMusicasResponse = optionalMusicasPorAlbum.get().stream()
                .map(m -> mapper.map(m, MusicaResponse.class))
                .collect(Collectors.toList());

            return new ResponseEntity<>(listMusicasResponse, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    // Retorna uma lista de musicas por meio de busca o titulo especificado no parametro do Get
    @GetMapping(value="/titulo/{titulo}")
    public ResponseEntity<List<MusicaResponse>> obterPorTitulo(@PathVariable String titulo) {
        List<MusicaDto> musicasEncontradas = service.obterMusicaPorTitulo(titulo).get();

        Optional<List<MusicaDto>> optionalMusicasEncontradas = Optional.of(musicasEncontradas);

        if(optionalMusicasEncontradas.isPresent()) {
            List<MusicaResponse> responses = optionalMusicasEncontradas.get()
            .stream()
            .map(msc -> mapper.map(msc, MusicaResponse.class))
            .collect(Collectors.toList());

            return new ResponseEntity<>(responses, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    
    //#endregion
    
    //#region @Posts

    // Recebe um Corpo JSON no formato Musica e armazena esse novo registro no BD
    @PostMapping    
    public ResponseEntity<MusicaResponse> criarMusica(@Valid @RequestBody MusicaRequest MusicaNova) {
        //TODO: process POST request
        MusicaDto dto = mapper.map(MusicaNova, MusicaDto.class);

        return new ResponseEntity<>(mapper.map(service.criarMusica(dto), MusicaResponse.class), HttpStatus.OK);
    }

    //#endregion

    
    //#region @Puts

    // Recebe um Corpo JSON no formato Musica e atualiza o registro ja existente, por meio do id
    @PutMapping(value="/{id}")
    public ResponseEntity<MusicaResponse> atualizarMusica(@PathVariable String id, @RequestBody MusicaResponse musicaResponse) {
        //TODO: process PUT request
        MusicaDto dto = mapper.map(musicaResponse, MusicaDto.class);
        musicaResponse = mapper.map(service.atualizarMusica(id, dto), MusicaResponse.class);
        
        return new ResponseEntity<>(musicaResponse, HttpStatus.OK);
    }

    //#endregion


    //#region @Deletes

    // Remove um registro existente pelo Id
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> excluirMusica(@PathVariable String id) {
        service.removerMusica(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    //#endregion


}
