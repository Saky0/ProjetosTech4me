package classes;

public class Produto {
    private int código;
    private String nome;
    private Double valor;
    private int qtdEstoque;

    //#region Getters and Setters
    public int getCódigo() {
        return código;
    }
    public void setCódigo(int código) {
        this.código = código;
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
    public void setValor(Double valor) {
        this.valor = valor;
    }
    public int getQtdEstoque() {
        return qtdEstoque;
    }
    public void setQtdEstoque(int qtdEstoque) {
        this.qtdEstoque = qtdEstoque;
    }
    //#endregion

    
    
}
