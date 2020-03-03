package BD;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {
    Person owner;
    String data;
    Date date;
    private static final DateFormat dateFormat = new SimpleDateFormat("YY/MM/dd HH:mm:ss");
    public Message(String data, Person person){
        this.owner = person;
        this.data = data;
        date = new Date();
    }

    public String getDate() {
        return dateFormat.format(date);
    }

    public Person getOwner() {
        return owner;
    }

    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        return ("[" + getDate() + "] " +  owner.toString() + " : " + getData());
    }
}
