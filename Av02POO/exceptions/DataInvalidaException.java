package exceptions;

import java.time.LocalDate;

public class DataInvalidaException extends Exception {

    public DataInvalidaException() {
                
        super(String.format("Data inválida! A data não pode ser posterior a data Atual: %s  !", LocalDate.now().toString()));
    }

    public DataInvalidaException(String exceptionEspecifica) {
        super(exceptionEspecifica);
    }
}
