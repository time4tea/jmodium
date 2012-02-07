package net.time4tea.asm.transform;

public class AdapterRuntimeException extends RuntimeException {

    public AdapterRuntimeException(String message) {
        super(new AdapterException(message));
    }

    public AdapterRuntimeException(String message, Throwable cause) {
        super(new AdapterException(message, cause));
    }

    public AdapterException cause() {
        return (AdapterException) getCause();
    }
}
