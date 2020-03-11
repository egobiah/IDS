package Server;


import App._Runnable;
import Server.ConfigManagerServices.ConfigManager;

import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server implements _Runnable {
    private int port;
    private String adresse;
    private Registry registry;
    private TalkService talkService;


    public Server(int port, String address, String savedPath) throws RemoteException, IOException {
        this.talkService = new TalkService(savedPath);
        this.port = port;
        this.adresse = address;
        LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
        this.registry = LocateRegistry.getRegistry(Registry.REGISTRY_PORT);
    }

    /**
     * This method allows to run the server. Need to call them first.
     * Initialize the rmi server by binding all the components
     * <p>
     *
     * @return 0 if the server start correctly
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public int run() throws IOException, ClassNotFoundException, InterruptedException {
        System.setProperty("java.security.policy", ConfigManager.getConfig("securityManagerProp"));

        if (System.getSecurityManager() == null) System.setSecurityManager(new RMISecurityManager());

        try {
            this.registry.rebind(this.buildRmiAddr(ChatComponent, this.adresse), this.talkService);
        } catch (RemoteException e) {
            System.out.println("Server Failed to start");
            System.exit(1);
        }
        this.talkService.boot();
        return 0;

    }

    /**
     * Method call when the server shutdown.
     * Unbind all services from the rmi registry to stop the server.
     *
     * @throws RemoteException
     * @throws NotBoundException
     * @throws SQLException
     */
    @Override
    public void stop() throws IOException, NotBoundException, InterruptedException {
        this.talkService.powerOff();
        this.registry.unbind(this.buildRmiAddr(ChatComponent, this.adresse));

        Logger.getGlobal().log(Level.INFO, "Power Off server");
    }
}


