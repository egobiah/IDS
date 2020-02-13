package Exceptions;

public class WrongFormatCli extends Exception {
    public WrongFormatCli(String errorMessage) {
        super(errorMessage);
    }
}