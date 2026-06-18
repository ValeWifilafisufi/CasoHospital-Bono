package CasoHospital.Bono.exception;

public class RecursoDuplicadoException extends RuntimeException{
    public RecursoDuplicadoException(String mensaje){
        super(mensaje);
    }
}