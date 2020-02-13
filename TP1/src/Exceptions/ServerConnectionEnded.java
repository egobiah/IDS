package Exceptions;

public class ServerConnectionEnded extends Exception {
    public ServerConnectionEnded(String errorMessage) {
        super(errorMessage);
    }
}