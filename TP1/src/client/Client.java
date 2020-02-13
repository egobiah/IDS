package client;

import Exceptions.WrongFormatCli;
import server.Calculator_itf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


public class Client  implements Connectable{
    private Socket echoSocket;
    private String hostName;
    private int portNumber;
    private BufferedReader stdIn;
    private BufferedReader in;
    private PrintWriter out;

    public Client(String hostName, int portNumber) {
        this.hostName = hostName;
        this.portNumber = portNumber;
        try {
            this.echoSocket = new Socket(this.hostName, this.portNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void run() throws IOException {
        String userInput;
        while ((userInput = this.stdIn.readLine()) != null) {
            String[] userInput_split = userInput.split(" ");
            try {
                if(userInput_split.length == 0) throw  new WrongFormatCli("no cmd found : " + userInput);
                switch (userInput_split[0]){
                    case "+":
                    case "-":
                    case "/":
                    case "*":
                        if(userInput_split.length != 3) throw  new WrongFormatCli("+ need 2 operands");
                        for (String str : userInput_split) this.out.println(str);
                    break;
                    case "PHONE":
                        if(userInput_split.length >= 3 && userInput_split.length <= 4)
                            throw  new WrongFormatCli("+ need 2 operands");
                        if((userInput_split[1].equals("GET") && userInput_split[2].equals("ALL")) ||
                                                userInput_split[1].equals("REMOVE")){
                            if( userInput_split.length > 3 )
                                throw new WrongFormatCli("unexpected cmd : " + userInput);
                        } else {
                            if (userInput_split.length != 4)
                                throw new WrongFormatCli("unexpected cmd : " + userInput);
                        }

                        for (String str : userInput_split) this.out.println(str);
                    break;
                    default:
                        throw new WrongFormatCli("unexpected cmd : " + userInput);
                }
            }catch (WrongFormatCli error){
                System.out.println(error.toString());
                continue;
            }
            System.out.println(this.in.readLine());
        }
    }

    public void connect() {
        try {
            this.out = new PrintWriter(this.echoSocket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(this.echoSocket.getInputStream()));
            this.stdIn = new BufferedReader(new InputStreamReader(System.in));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + this.hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    this.hostName);
            System.exit(1);
        }
        System.out.println("Connected");
    }
}
