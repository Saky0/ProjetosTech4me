package classes;

import java.time.LocalDate;
import exceptions.DataInvalida;

public class Venda {
    private LocalDate dataVenda;
    private Produto produtoVendido;
    private int qtdProdutoVendido;

    public LocalDate getDataVenda() {
        return dataVenda;
    }

    // Tratamento para Evitar que um data posterior seja incluida como data de Venda
    public void setDataVenda(LocalDate dataVenda) throws DataInvalida {
        if(dataVenda.isAfter(LocalDate.now())) {
            throw new DataInvalida();
        }

        this.dataVenda = dataVenda;
    }

    public Produto getProdutoVendido() {
        return produtoVendido;
    }
    public void setProdutoVendido(Produto produtoVendido) {
        this.produtoVendido = produtoVendido;
    }
    public int getQtdProdutoVendido() {
        return qtdProdutoVendido;
    }
    public void setQtdProdutoVendido(int qtdProdutoVendido) {
        this.qtdProdutoVendido = qtdProdutoVendido;
    }


    
}
