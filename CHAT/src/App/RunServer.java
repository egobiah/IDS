package App;

import java.util.Scanner;

import Server.Server;

public class RunServer {

    public static void main(String[] args) {
        try {
            Server server = new Server(_Runnable.port, _Runnable.address,"ChatRoom");
            server.run();


            System.out.println("Q to quit server");
            Scanner sc = new Scanner(System.in);
            String command = sc.nextLine();

            if (command.equals("Q")) {
                server.stop();
                System.exit(0);
            }

        } catch (Exception e) {
            System.err.println("Error on server :" + e);
            e.printStackTrace();
        }
    }



}
