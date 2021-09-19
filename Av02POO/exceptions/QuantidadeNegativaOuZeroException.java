package exceptions;

public class QuantidadeNegativaOuZeroException extends Exception{
    
    public QuantidadeNegativaOuZeroException() {
        super("Digite apenas valores positivos ou maiores que 0 !!");
    }

    public QuantidadeNegativaOuZeroException(String string) {
        super(string);
    }
}
