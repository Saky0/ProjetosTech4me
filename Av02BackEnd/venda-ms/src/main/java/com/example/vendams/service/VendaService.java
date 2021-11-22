package com.example.vendams.service;

import java.util.List;
import java.util.Optional;

import com.example.vendams.shared.PeriodoDeVendasDto;
import com.example.vendams.shared.VendaDto;

public interface VendaService {
    

    //
    //#region GETS

    // Obtem uma lista com todas as vendas registrada
    List<VendaDto> obterVendas();

    // Obtem uma única venda pelo seu Id
    Optional<VendaDto> obterVendaPorId(String id);

    // Obtem uma lista de Vendas dentro de um período delimitado
    Optional<List<VendaDto>> obterVendaPorPeriodo(PeriodoDeVendasDto periodo);

    //#endregion

    //
    //#region POSTS

    // inseri uma nova no banco de Dados
    VendaDto inserirVenda(VendaDto dto);

    //#endregion

    //
    //#region PUTS

    // Atualizar um registro de venda
    VendaDto atualizarVenda(VendaDto venda);

    //#endregion

    //
    //#region DELETES

    // Remove uma venda por meio de um id
    void removerVenda(String id);

    //#endregion
}
