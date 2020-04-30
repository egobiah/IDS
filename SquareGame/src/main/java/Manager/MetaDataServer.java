package Manager;

import java.util.HashMap;

public class MetaDataServer {
    private int nbLocalSever;
    private HashMap<String,Integer> serverListInfo = null;

    public MetaDataServer() {
        this.nbLocalSever = 0;
    }

    public void addServer(String ip, Integer port){
        if(ip.equals("auto")) this.nbLocalSever++;
        this.serverListInfo.put(ip, port);
    }

    public int getNbLocalSever() {
        return nbLocalSever;
    }

    public HashMap<String, Integer> getServerListInfo() {
        return serverListInfo;
    }
}
