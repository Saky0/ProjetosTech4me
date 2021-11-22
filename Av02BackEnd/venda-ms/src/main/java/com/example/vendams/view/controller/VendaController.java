package com.example.vendams.view.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.example.vendams.service.VendaService;
import com.example.vendams.shared.PeriodoDeVendasDto;
import com.example.vendams.shared.VendaDto;
import com.example.vendams.view.model.VendaRequest;
import com.example.vendams.view.model.VendaResponse;

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
@RequestMapping("/api/vendas")
public class VendaController {

    @Autowired
    private VendaService service;

    private ModelMapper mapper = new ModelMapper();
    
    @PostMapping
    public ResponseEntity<VendaResponse> inserirVenda(@RequestBody VendaRequest vendaNova) {
        //TODO: process POST request
        VendaDto dto = service.inserirVenda(mapper.map(vendaNova, VendaDto.class));

        return new ResponseEntity<>(mapper.map(dto, VendaResponse.class), HttpStatus.OK);
    }
    
    // Obtem uma lista com todas as vendas registrada
    @GetMapping
    public ResponseEntity<List<VendaResponse>> obterVendas() {
        // TODO Auto-generated method stub
        List<VendaResponse> listResponses = service.obterVendas().stream()
            .map(venda -> mapper.map(venda, VendaResponse.class))
            .collect(Collectors.toList());
        
        return new ResponseEntity<>(listResponses, HttpStatus.ACCEPTED);
    }

    // Atualizar um registro de venda
    @PutMapping(value = "/{id}")
    public ResponseEntity<VendaResponse> atualizarVenda(@PathVariable String id, @RequestBody @Valid VendaResponse venda) {
        // TODO Auto-generated method stub
        venda.setId(id);

        VendaDto vendaAtualizada = service.atualizarVenda(mapper.map(venda, VendaDto.class));

        return new ResponseEntity<>(mapper.map(vendaAtualizada, VendaResponse.class), HttpStatus.OK);
    }

    // Remove uma venda por meio de um id
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> removerVenda(@PathVariable String id) {
        service.removerVenda(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Obtem uma única venda pelo seu Id
    @GetMapping(value = "/{id}")
    public ResponseEntity<VendaResponse> obterVendaPorId(@PathVariable String id) {
        // TODO Auto-generated method stub
        Optional<VendaDto> vendaEncontradaOptional = service.obterVendaPorId(id);

        if(vendaEncontradaOptional.isPresent()) {
            VendaResponse dto = mapper.map(vendaEncontradaOptional.get(), VendaResponse.class);

            return new ResponseEntity<>(dto, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Obtem uma lista de Vendas dentro de um período delimitado
    /*
        Utiliza a Classe PeriodoDeVendasDto como uma forma de receber um @RequestBody com a dataInicial
        e a dataFinal
    */
    @GetMapping(value = "/periodo")
    public ResponseEntity<List<VendaResponse>> obterVendaPorPeriodo(@RequestBody PeriodoDeVendasDto periodo) {
        // TODO Auto-generated method stub
        List<VendaDto> listVendas = service.obterVendaPorPeriodo(periodo).get();

        Optional<List<VendaDto>> optionalVendas = Optional.of(listVendas);

        if(optionalVendas.isPresent()) {
            List<VendaResponse> listResponseVendas = optionalVendas.get().stream()
            .map(venda -> mapper.map(venda, VendaResponse.class))
            .collect(Collectors.toList());

            return new ResponseEntity<>(listResponseVendas, HttpStatus.FOUND);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
