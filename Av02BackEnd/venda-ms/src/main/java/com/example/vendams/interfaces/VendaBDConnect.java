package com.example.vendams.interfaces;

import java.util.List;

import com.example.vendams.shared.VendaDto;

public interface VendaBDConnect {
    /*
        A interface VendaBDConnect serve para implementar os métodos em quaisquer tipo de conexão com o BD
        podendo assim alterar a ClasseImpl com o tipo de conexão desejada e mantendo o retorno e parâmetros 
        dos métodos
    */
    List<VendaDto> obterTodasVendas();

    List<VendaDto> obterVendasPorPeriodo();

    VendaDto inserirNovaVenda(VendaDto vendaNova) throws Exception;

}
