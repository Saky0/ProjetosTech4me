package classes;

import java.util.Comparator;

public class CompararProdutosPorValor implements Comparator<Produto>{

    @Override
    public int compare(Produto p1, Produto p2) {
        // TODO Auto-generated method stub
        return p1.getValor().compareTo(p2.getValor());
    }
    
}
