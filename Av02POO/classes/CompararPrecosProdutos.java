package classes;

import java.util.Comparator;

public class CompararPrecosProdutos implements Comparator<Produto>{

    @Override
    public int compare(Produto produto, Produto outroProduto) {
        return produto.getValor().compareTo(outroProduto.getValor());
    }
    
}
