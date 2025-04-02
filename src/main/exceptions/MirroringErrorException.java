package main.exceptions;

public class MirroringErrorException extends RuntimeException {
    public MirroringErrorException(String message) {
        super(message);
    }

    public MirroringErrorException() {
        super("Mirroring error");
    }
}
