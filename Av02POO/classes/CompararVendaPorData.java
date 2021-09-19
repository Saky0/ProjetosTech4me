package classes;

import java.util.Comparator;

public class CompararVendaPorData implements Comparator<Venda>{

    @Override
    public int compare(Venda v1, Venda v2) {
        int difHora = v1.getDataVenda().getYear() - v2.getDataVenda().getYear();

        if(difHora != 0) {
            return difHora;
        }

        int difMinuto = v1.getDataVenda().getMonthValue() - v2.getDataVenda().getMonthValue();

        if(difMinuto != 0) {
            return difMinuto;
        }

        int difSegundos = v1.getDataVenda().getDayOfMonth() - v2.getDataVenda().getDayOfMonth();

        return difSegundos;
    }
    
}
