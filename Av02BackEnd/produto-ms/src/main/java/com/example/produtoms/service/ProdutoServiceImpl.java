package com.example.produtoms.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.produtoms.model.Produto;
import com.example.produtoms.repository.ProdutoRepository;
import com.example.produtoms.shared.ProdutoDto;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProdutoServiceImpl implements ProdutoService{

    @Autowired
    private ProdutoRepository repository;

    private ModelMapper mapper = new ModelMapper();

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
    // Devolve como retorno o novo objeto cadastrado
    @Override
    public ProdutoDto inserirNovoProduto(ProdutoDto dto) {
        // TODO Auto-generated method stub
        Produto produtoNovo = mapper.map(dto, Produto.class);

        return mapper.map(repository.save(produtoNovo), ProdutoDto.class);
    }

    // Recebe um id do produto a ser atualizado e o corpo JSON do produto
    @Override
    public ProdutoDto atualizarNovoProduto(String id, ProdutoDto dto) {
        // TODO Auto-generated method stub
        dto.setId(id);  

        Produto produtoAtualizado = mapper.map(dto, Produto.class);

        return mapper.map(repository.save(produtoAtualizado), ProdutoDto.class);
    }

    // Remove um produto existente por um Id
    @Override
    public void removerProdutoPorId(String id) {
        // TODO Auto-generated method stub
        repository.deleteById(id);
    }

    // Obtem o produto por id e retorna o mesmo
    @Override
    public Optional<ProdutoDto> obterProdutoPorId(String id) {
        // TODO Auto-generated method stub
        Optional<Produto> optional = repository.findById(id);

        if(optional.isPresent()) {
            
            return Optional.of(mapper.map(optional.get(), ProdutoDto.class));
        }

        return Optional.empty();
    }

    /*
        Recebe o nome em comum e procura no DB usando o novo método criado no Repository "obterPorNomeEmComum(String nome)""
    */
    @Override
    public Optional<List<ProdutoDto>> obterPorNomeEmComum(String nome) {
        // TODO Auto-generated method stub
        List<Produto> produtosEncontrados = repository.obterPorNomeEmComum(nome);

        Optional<List<Produto>> optionalEncontrado = Optional.of(produtosEncontrados);

        if(optionalEncontrado.isPresent()) {
            List<ProdutoDto> listDtos = optionalEncontrado.get().stream()
                .map(produto -> mapper.map(produto, ProdutoDto.class))
                .collect(Collectors.toList());

            return Optional.of(listDtos);
        }

        return Optional.empty();
    }

    // Busca um produto pelo seu respectivo código, este definido no momento de seu cadastro
    @Override
    public Optional<ProdutoDto> obterProdutoPorCodigo(int codigo) {
        // TODO Auto-generated method stub
        Optional<Produto> optional = repository.findByCodigo(codigo);

        if(optional.isPresent()) {
            
            return Optional.of(mapper.map(optional.get(), ProdutoDto.class));
        }

        return Optional.empty();
    }
    
}
