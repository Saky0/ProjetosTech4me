package exceptions;

public class CodigoNegativoException extends Exception{
    public CodigoNegativoException() {
        super("O Código do produto não pode ser um valor negativo! ");
    }
}
