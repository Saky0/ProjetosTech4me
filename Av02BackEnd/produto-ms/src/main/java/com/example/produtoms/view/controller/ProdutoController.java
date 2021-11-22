package com.example.produtoms.view.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.example.produtoms.service.ProdutoService;
import com.example.produtoms.shared.ProdutoDto;
import com.example.produtoms.view.model.ProdutoRequest;
import com.example.produtoms.view.model.ProdutoResponse;

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

    // Inseri um novo Produto, mapeando o produto recebido para o objeto adequado
    // Devolve como retorno o novo objeto cadastrado
    @PostMapping
    public ResponseEntity<ProdutoResponse> inserirNovoProduto(@RequestBody @Valid ProdutoRequest produtoRequest) {
        //TODO: process POST request
        ProdutoDto dtoNovo = service.inserirNovoProduto(mapper.map(produtoRequest, ProdutoDto.class));

        return new ResponseEntity<>(mapper.map(dtoNovo, ProdutoResponse.class), HttpStatus.OK);
    }
    
    // Recebe um id do produto a ser atualizado e o corpo JSON do produto
    @PutMapping(value = "/{id}")
    public ResponseEntity<ProdutoResponse> atualizarNovoProduto(@PathVariable String id, @RequestBody @Valid ProdutoRequest request) {
        // TODO Auto-generated method stub
        ProdutoDto dto = mapper.map(request, ProdutoDto.class);
        dto.setId(id);

        dto = service.atualizarNovoProduto(id, dto);

        return new ResponseEntity<>(mapper.map(dto, ProdutoResponse.class), HttpStatus.OK);
    }

    // Remove um produto existente por um Id
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> removerProdutoPorId(@PathVariable String id) {
        // TODO Auto-generated method stub
        service.removerProdutoPorId(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Obtem o produto por id e retorna o mesmo
    @GetMapping(value = "/idp/{id}")
    public ResponseEntity<ProdutoResponse> obterProdutoPorId(@PathVariable String id) {
        // TODO Auto-generated method stub
        Optional<ProdutoDto> produtoEncontrado = service.obterProdutoPorId(id);

        if(produtoEncontrado.isPresent()) {

            ProdutoResponse response = mapper.map(produtoEncontrado.get(), ProdutoResponse.class);

            return new ResponseEntity<>(response, HttpStatus.FOUND);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /*
        Recebe o nome em comum e procura no DB usando o novo método criado no Repository "obterPorNomeEmComum(String nome)""
    */
    @GetMapping(value = "/nome/{nome}")
    public ResponseEntity<List<ProdutoResponse>> obterPorNomeEmComum(@PathVariable String nome) {
        // TODO Auto-generated method stub
        List<ProdutoDto> produtosEncontrados = service.obterPorNomeEmComum(nome).get();

        Optional<List<ProdutoDto>> optional = Optional.of(produtosEncontrados);

        if(optional.isPresent()) {
            List<ProdutoResponse> listResponses = optional.get().stream()
                .map(produto -> mapper.map(produto, ProdutoResponse.class))
                .collect(Collectors.toList());

            return new ResponseEntity<>(listResponses, HttpStatus.FOUND);
        }
        
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
