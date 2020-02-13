package Exceptions;

public class ServerConnectionLost extends Exception {
    public ServerConnectionLost(String errorMessage) {
        super(errorMessage);
    }
}