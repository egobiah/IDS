package App;


public class RunServer {

    public static void main(String[] args) {
        try {
            //MQServer MQServer = new MQServer(_Runnable.port, _Runnable.address,".");
            //MQServer.run();


            System.out.println("Q to quit MQServer");
            /*Scanner sc = new Scanner(System.in); // not compatible with docker
            String command = sc.nextLine();

            if (command.equals("Q")) {
                MQServer.stop();
                System.exit(0);
            }*/

        } catch (Exception e) {
            System.err.println("Error on server :" + e);
            e.printStackTrace();
        }
    }



}
