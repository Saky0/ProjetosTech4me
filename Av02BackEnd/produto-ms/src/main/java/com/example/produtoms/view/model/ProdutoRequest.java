package com.example.produtoms.view.model;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

public class ProdutoRequest {
    @Min(value = 1, message = "O código deve ser > 0")
    private int codigo;
    @NotBlank(message = "A descrição Não pode conter somente espaços!")
    @NotEmpty(message = "A descrição deve ser preenchida")
    private String descricao;

    /*
        O preço minimo de um produto é 0.01 no caso de cadastrar um produto muito barato como uma bala
    */
    @DecimalMin(value = "0.01", message = "O Valor do Produto deve ser > 0")
    private Double valor;

    @Min(value = 1, message = "A quantidade do Produto deve ser >= 1")
    private int quantidadeEstoque;
    
    public int getCodigo() {
        return codigo;
    }
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public Double getValor() {
        return valor;
    }
    public void setValor(Double valor) {
        this.valor = valor;
    }
    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }
    public void setQuantidadeEstoque(int quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

}
