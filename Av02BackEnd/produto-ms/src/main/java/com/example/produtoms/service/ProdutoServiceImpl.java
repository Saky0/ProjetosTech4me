package com.example.produtoms.service;

import java.util.List;
import java.util.stream.Collectors;

import com.example.produtoms.model.Produto;
import com.example.produtoms.repository.ProdutoRepository;
import com.example.produtoms.shared.ProdutoDto;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProdutoServiceImpl implements ProdutoService{

    
    private ModelMapper mapper = new ModelMapper();

    @Autowired
    private ProdutoRepository repository;

    // Obtem todos os produtos por um Get sem parâmetros
    @Override
    public List<ProdutoDto> obterTodosProdutos() {
        // TODO Auto-generated method stub
        List<ProdutoDto> listaDto = repository.findAll().stream()
            .map(p -> mapper.map(p, ProdutoDto.class))
            .collect(Collectors.toList());

        return listaDto;
    }

    // Inseri um novo Produto, mapeando o produto recebido para o objeto adequado
    @Override
    public ProdutoDto inserirNovoProduto(ProdutoDto dto) {
        // TODO Auto-generated method stub
        Produto produtoNovo = mapper.map(dto, Produto.class);

        return mapper.map(repository.save(produtoNovo), ProdutoDto.class);
    }

    @Override
    public ProdutoDto atualizarNovoProduto(String id, ProdutoDto dto) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void removerProdutoPorId() {
        // TODO Auto-generated method stub
        
    }
    
}
