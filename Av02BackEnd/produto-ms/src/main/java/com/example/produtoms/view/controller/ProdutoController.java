package com.example.produtoms.view.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.example.produtoms.service.ProdutoService;
import com.example.produtoms.view.model.ProdutoResponse;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService service;

    private ModelMapper mapper = new ModelMapper();
    
    @GetMapping
    public ResponseEntity<List<ProdutoResponse>> obterTodosProdutos() {
        List<ProdutoResponse> listaResponse = service.obterTodosProdutos().stream()
            .map(p -> mapper.map(p, ProdutoResponse.class))
            .collect(Collectors.toList());

        return new ResponseEntity<>(listaResponse, HttpStatus.OK);
    }
}
