package Hello;

import java.io.Serializable;

public class Client implements Serializable {
    String name;
    String adress;



    public  Client(String name, String address){
        this.name = name;
        this.adress = address;
    }

    public String getAdress() {
        return adress;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + adress;
    }
}
