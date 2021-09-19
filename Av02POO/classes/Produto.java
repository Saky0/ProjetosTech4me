package classes;

import exceptions.CodigoNegativoException;
import exceptions.QuantidadeNegativaOuZeroException;

public class Produto {
    private Integer codigo;
    private String nome;
    private Double valor;
    private int qtdEstoque;

    //#region Getters and Setters
    public Integer getCodigo() {
        return codigo;
    }
    public void setCodigo(Integer codigo) throws CodigoNegativoException{
        if(codigo < 0) {
            throw new CodigoNegativoException();
        }
        this.codigo = codigo;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public Double getValor() {
        return valor;
    }
    public void setValor(Double valor) throws QuantidadeNegativaOuZeroException{

        if(valor < 1) {
            throw new QuantidadeNegativaOuZeroException();
        }
        this.valor = valor;
    }
    public int getQtdEstoque() {
        return qtdEstoque;
    }
    public void setQtdEstoque(int qtdEstoque) throws QuantidadeNegativaOuZeroException{

        if(qtdEstoque < 0) {
            throw new QuantidadeNegativaOuZeroException();
        }

        this.qtdEstoque = qtdEstoque;
    }
    //#endregion

    public Produto(Integer codigo, String nome, Double valor, int qtdEstoque) {
        this.codigo = codigo;
        this.nome = nome;
        this.valor = valor;
        this.qtdEstoque = qtdEstoque;
    }

    public Produto() {

    }

    
    
    
}
