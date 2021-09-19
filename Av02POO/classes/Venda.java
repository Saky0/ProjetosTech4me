package classes;

import java.time.LocalDate;
import exceptions.DataInvalidaException;
import exceptions.QuantidadeNegativaOuZeroException;

public class Venda {
    private LocalDate dataVenda;
    private Produto produtoVendido;
    private Integer qtdProdutoVendido;

    public LocalDate getDataVenda() {
        return dataVenda;
    }

    // Tratamento para Evitar que um data posterior seja incluida como data de Venda
    public void setDataVenda(LocalDate dataVenda) throws DataInvalidaException {
        if(dataVenda.isAfter(LocalDate.now())) {
            throw new DataInvalidaException();
        }
        

        this.dataVenda = dataVenda;
    }

    public Produto getProdutoVendido() {
        return produtoVendido;
    }
    public void setProdutoVendido(Produto produtoVendido) {
        this.produtoVendido = produtoVendido;
    }
    public Integer getQtdProdutoVendido() {
        return qtdProdutoVendido;
    }
    public void setQtdProdutoVendido(Integer qtdProdutoVendido) throws QuantidadeNegativaOuZeroException{

        if(qtdProdutoVendido <= 0) {
            throw new QuantidadeNegativaOuZeroException();
        }
        if(produtoVendido.getQtdEstoque() < qtdProdutoVendido) {
            throw new QuantidadeNegativaOuZeroException("O produto selecionado nao possui esta quantia no estoque: " + qtdProdutoVendido +
            "\nQuantidade Atual: " + produtoVendido.getQtdEstoque());
        }

        this.qtdProdutoVendido = qtdProdutoVendido;
    }

    public Venda() {

    }

    public Venda(LocalDate dataVenda, Produto produtoVendido, Integer qtdProdutoVendido) {
        this.dataVenda = dataVenda;
        this.produtoVendido = produtoVendido;
        this.qtdProdutoVendido = qtdProdutoVendido;
    }

    /*
        O método é chamado somente após todas as etapas do cadastro de uma venda estarem completas
        Assim, da baixa no sistema a quantidade do produto vendido
        Usando o conceito de alocação de memória, apenas alterando o valor presente no endereço do produto cadastrado na venda
        ja vai alterar a sua quantidade na lista
    */
    public void finalizarVenda() throws QuantidadeNegativaOuZeroException {
        produtoVendido.setQtdEstoque(produtoVendido.getQtdEstoque() - qtdProdutoVendido);
    }
    
}
