package exceptions;

import java.time.LocalDate;

public class DataInvalida extends Exception {

    public DataInvalida() {
                
        super(String.format("Data inválida! A data não pode ser posterior a data Atual: %s  !", LocalDate.now().toString()));
    }
}
