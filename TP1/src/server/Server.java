package server;

import Exceptions.ServerConnectionEnded;
import Exceptions.ServerConnectionLost;
import Exceptions.WrongFormatCli;
import PhoneRegister.Person;
import PhoneRegister.PersonManager;
import client.Connectable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server implements Connectable {
    private int port;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private PersonManager manager;

    public Server(int port) throws IOException {
        this.port = port;
        this.serverSocket = new ServerSocket(this.port);
        this.manager = new PersonManager();
    }

    public void connect() {
        try {
            this.clientSocket = this.serverSocket.accept();
            this.out = new PrintWriter(this.clientSocket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            } catch(IOException e){
                System.out.println("Exception caught when trying to listen on port "
                        + this.port + " or listening for a connection");
                System.out.println(e.getMessage());
            }
        System.out.println("Connected");
    }

    public void run() throws IOException, ServerConnectionEnded, ServerConnectionLost {
        String cli = "";
        List<Integer> args = null;
        List<String> argStrings = null;
        String phone_cmd = null;
        while (!this.clientSocket.isClosed()){
            try {
                cli = this.get_answer();
            } catch (ServerConnectionEnded | ServerConnectionLost serverConnectionEnded) {
                this.serverSocket.close();
            }
            try {
                switch (cli) {
                    case "PHONE":
                        phone_cmd = (this.get_strings(1)).get(0);
                        switch (phone_cmd){
                            case "GET":
                                phone_cmd = (this.get_strings(1)).get(0);
                                if(phone_cmd.equals("FILTER")){
                                    phone_cmd = (this.get_strings(1)).get(0);
                                    switch (phone_cmd){
                                        case "NUMBER":
                                            this.out.println(
                                                    this.manager.getPersonByPhone(
                                                            (this.get_strings(1)).get(0)
                                                    )
                                            );
                                        break;
                                        case "NAME":
                                            this.out.println(
                                                    this.manager.getPersonByName(
                                                            (this.get_strings(1)).get(0)
                                                    )
                                            );
                                        break;
                                        case "UID":
                                            this.out.println(
                                                    this.manager.getPersonByUid(
                                                            (Integer) (this.get_ints(1)).get(0)
                                                    )
                                            );
                                        break;
                                        default:
                                            System.out.println("Bad filter");
                                        break;

                                    }
                                    return;
                                }
                                this.out.println(this.manager.printAll());

                            break;
                            case "ADD":
                                argStrings = this.get_strings(2);
                                this.manager.addPerson(
                                        new Person(argStrings.get(0), argStrings.get(1))
                                );
                            break;
                            case "REMOVE":
                                this.manager.removePerson(
                                        this.manager.getPersonByUid((Integer) this.get_ints(1).get(1))
                                );
                            break;
                            default:
                            break;
                        }

                    break;
                    case "+":
                        args = this.get_ints(2);
                        this.out.println("result = " +
                                Calculator_itf.plus( args.get(0),  args.get(1)));
                    break;
                    case "-":
                        args = this.get_ints(2);
                        this.out.println("result = " +
                                Calculator_itf.minus( args.get(0),  args.get(1)));
                        break;
                    case "/":
                        args = this.get_ints(2);
                        this.out.println("result = " +
                                Calculator_itf.divide( args.get(0),  args.get(1)));
                        break;
                    case "*":
                        args = this.get_ints(2);
                        this.out.println("result = " +
                                Calculator_itf.multiply( args.get(0),  args.get(1)));
                        break;
                    default:
                        args = null;
                        throw new WrongFormatCli("");
                }
            }catch (WrongFormatCli error){
                this.flush(this.in);
                this.out.println("ServerErrorUnexpected { " + error + " }");
            }catch (java.lang.ArithmeticException error){
                this.flush(this.in);
                this.out.println(error.toString());
            }
        }
    }

    private void flush(BufferedReader reader){
        try {
            reader.mark(0);
            reader.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
       /* while (true) {
            try {
                if (reader.readLine() == null) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
    }

    private List<String> get_strings(int nb_string) throws ServerConnectionEnded, ServerConnectionLost {
        List args = new ArrayList<String>();
        for(int i = 0; i < nb_string; i++) args.add(this.get_answer());
        return args;
    }

    private List get_ints(int nb_string) throws ServerConnectionEnded, ServerConnectionLost, WrongFormatCli {
        boolean exeption = false;
        List args = new ArrayList<Integer>();
        for(int i = 0; i < nb_string; i++){
            try {
                args.add(Integer.parseInt(this.get_answer()));
            }catch (NumberFormatException error){
                exeption = true;
            }
        }
        if(exeption) throw new WrongFormatCli("invalid arguments");
        return args;
    }

    private String get_answer() throws ServerConnectionEnded, ServerConnectionLost {
        String cli;
            try {
                cli = this.in.readLine();
                if(cli == null)
                    throw new ServerConnectionEnded("ciao");
            } catch (IOException e) {
                e.printStackTrace();
                throw new ServerConnectionLost("snifsnif");
            }
        System.out.println("anwser : " + cli );
        return cli;
    }

}
