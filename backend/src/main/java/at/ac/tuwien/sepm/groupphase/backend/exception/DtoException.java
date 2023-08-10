package at.ac.tuwien.sepm.groupphase.backend.exception;

public class DtoException extends RuntimeException {

    public DtoException() {
    }

    public DtoException(String message) {
        super(message);
    }

    public DtoException(String message, Throwable cause) {
        super(message, cause);
    }

    public DtoException(Exception e) {
        super(e);
    }
}
